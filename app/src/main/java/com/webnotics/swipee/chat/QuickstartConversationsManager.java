package com.webnotics.swipee.chat;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.ErrorInfo;
import com.twilio.conversations.Message;
import com.twilio.conversations.Participant;
import com.twilio.conversations.StatusListener;
import com.twilio.conversations.User;
import com.webnotics.swipee.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface QuickstartConversationsManagerListener {
    void receivedNewMessage();

    void messageSentCallback();

    void reloadMessages();
}

interface TokenResponseListener {
    void receivedTokenResponse(boolean success, @Nullable Exception exception);
}

interface AccessTokenListener {
    void receivedAccessToken(@Nullable String token, @Nullable Exception exception);
}

interface AccessTokenAPI {

    @FormUrlEncoded
    @POST("appointments/access_token_chat/")
    Call<JsonObject> createAccessToken(@Field("user_name") String user_name);
}

public class QuickstartConversationsManager {

    // This is the unique name of the conversation  we are using
    private final static String DEFAULT_CONVERSATION_NAME = "testing";

    final private ArrayList<Message> messages = new ArrayList<>();

    private ConversationsClient conversationsClient;

    private Conversation conversation;

    private QuickstartConversationsManagerListener conversationsManagerListener;

    private String tokenURL = "";
    private String user_name = "";

    private static class TokenResponse {
        String token;
    }

    private final ConversationsClientListener mConversationsClientListener =
            new ConversationsClientListener() {

                @Override
                public void onConversationAdded(Conversation conversation) {
                    Log.i(MainChatActivity.TAG, "on conversation added");
                }

                @Override
                public void onConversationUpdated(Conversation conversation, Conversation.UpdateReason updateReason) {
                    Log.i(MainChatActivity.TAG, "on conversation updated");
                }

                @Override
                public void onConversationDeleted(Conversation conversation) {
                    Log.i(MainChatActivity.TAG, "on conversation deleted");
                }

                @Override
                public void onConversationSynchronizationChange(Conversation conversation) {
                    Log.i(MainChatActivity.TAG, "on conversation synchronization");
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.i(MainChatActivity.TAG, "on Error " + errorInfo.getMessage());
                }

                @Override
                public void onUserUpdated(User user, User.UpdateReason updateReason) {
                    Log.i(MainChatActivity.TAG, "on user updated " + user);
                }

                @Override
                public void onUserSubscribed(User user) {
                    Log.i(MainChatActivity.TAG, "on user subscribed");
                }

                @Override
                public void onUserUnsubscribed(User user) {
                    Log.i(MainChatActivity.TAG, "on conversation unsubscribed");
                }

                @Override
                public void onClientSynchronization(ConversationsClient.SynchronizationStatus synchronizationStatus) {
                    if (synchronizationStatus == ConversationsClient.SynchronizationStatus.COMPLETED) {
                        Log.i(MainChatActivity.TAG, "synchronization status completed");
                        loadChannels();
                    }
                }

                @Override
                public void onNewMessageNotification(String s, String s1, long l) {
                    Log.i(MainChatActivity.TAG, "on new message notification " + s);
                }

                @Override
                public void onAddedToConversationNotification(String s) {
                    Log.i(MainChatActivity.TAG, "on added to conversation notification");
                }

                @Override
                public void onRemovedFromConversationNotification(String s) {
                    Log.i(MainChatActivity.TAG, "on removed from conversation notification");
                }

                @Override
                public void onNotificationSubscribed() {
                    Log.i(MainChatActivity.TAG, "on notification subscribed");
                }

                @Override
                public void onNotificationFailed(ErrorInfo errorInfo) {
                    Log.i(MainChatActivity.TAG, "on notification failed");
                }

                @Override
                public void onConnectionStateChange(ConversationsClient.ConnectionState connectionState) {
                    Log.i(MainChatActivity.TAG, "on connection state change");
                }

                @Override
                public void onTokenExpired() {
                    Log.i(MainChatActivity.TAG, "on token expired");
                    retrieveToken(new AccessTokenListener() {
                        @Override
                        public void receivedAccessToken(@Nullable String token, @Nullable Exception exception) {
                            if (token != null) {
                                conversationsClient.updateToken(token, new StatusListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(MainChatActivity.TAG, "Refreshed access token.");
                                    }
                                });
                            }
                        }
                    });
                }

                @Override
                public void onTokenAboutToExpire() {
                    retrieveToken(new AccessTokenListener() {
                        @Override
                        public void receivedAccessToken(@Nullable String token, @Nullable Exception exception) {
                            if (token != null) {
                                conversationsClient.updateToken(token, new StatusListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(MainChatActivity.TAG, "Refreshed access token.");
                                    }
                                });
                            }
                        }
                    });
                }
            };

    private final CallbackListener<ConversationsClient> mConversationsClientCallback =
            new CallbackListener<ConversationsClient>() {
                @Override
                public void onSuccess(ConversationsClient conversationsClient) {
                    QuickstartConversationsManager.this.conversationsClient = conversationsClient;
                    conversationsClient.addListener(QuickstartConversationsManager.this.mConversationsClientListener);
                    Log.d(MainChatActivity.TAG, "Success creating Twilio Conversations Client");
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.e(MainChatActivity.TAG, "Error creating Twilio Conversations Client: " + errorInfo.getMessage());
                }
            };


    private final ConversationListener mDefaultConversationListener = new ConversationListener() {


        @Override
        public void onMessageAdded(final Message message) {
            Log.d(MainChatActivity.TAG, "Message added");
            messages.add(message);
            if (conversationsManagerListener != null) {
                conversationsManagerListener.receivedNewMessage();
            }
        }

        @Override
        public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {
            Log.d(MainChatActivity.TAG, "Message updated: " + message.getBody());

        }

        @Override
        public void onMessageDeleted(Message message) {
            Log.d(MainChatActivity.TAG, "Message deleted");
        }

        @Override
        public void onParticipantAdded(Participant participant) {
            Log.d(MainChatActivity.TAG, "Participant added: " + participant.getIdentity());
        }

        @Override
        public void onParticipantUpdated(Participant participant, Participant.UpdateReason updateReason) {
            Log.d(MainChatActivity.TAG, "Participant updated: " + participant.getIdentity() + " " + updateReason.toString());
        }

        @Override
        public void onParticipantDeleted(Participant participant) {
            Log.d(MainChatActivity.TAG, "Participant deleted: " + participant.getIdentity());
        }

        @Override
        public void onTypingStarted(Conversation conversation, Participant participant) {
            Log.d(MainChatActivity.TAG, "Started Typing: " + participant.getIdentity());
        }

        @Override
        public void onTypingEnded(Conversation conversation, Participant participant) {
            Log.d(MainChatActivity.TAG, "Ended Typing: " + participant.getIdentity());
        }

        @Override
        public void onSynchronizationChanged(Conversation conversation) {

        }
    };

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setListener(QuickstartConversationsManagerListener listener) {
        this.conversationsManagerListener = listener;
    }

    void retrieveAccessTokenFromServer(final Context context, String identity,
                                       final TokenResponseListener listener) {

        // Set the chat token URL in your strings.xml file
        String chatTokenURL = context.getString(R.string.chat_token_url);

        if ("https://YOUR_DOMAIN_HERE.twil.io/chat-token".equals(chatTokenURL)) {
            listener.receivedTokenResponse(false, new Exception("You need to replace the chat token URL in strings.xml"));
            return;
        }

        // tokenURL = chatTokenURL + "?user_name=" + identity;
        user_name = identity;
        tokenURL = chatTokenURL;

        new Thread(new Runnable() {
            @Override
            public void run() {
                retrieveToken(new AccessTokenListener() {
                    @Override
                    public void receivedAccessToken(@Nullable String token,
                                                    @Nullable Exception exception) {
                        if (token != null) {
                            ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
                            ConversationsClient.create(context, token, props, mConversationsClientCallback);
                            listener.receivedTokenResponse(true, null);
                        } else {
                            listener.receivedTokenResponse(false, exception);
                        }
                    }
                });
            }
        }).start();
    }

    void initializeWithAccessToken(final Context context, final String token) {

        ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
        ConversationsClient.create(context, token, props, mConversationsClientCallback);
    }

    private void retrieveToken(AccessTokenListener listener) {
        // OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(tokenURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AccessTokenAPI accessTokenAPI = retrofit.create(AccessTokenAPI.class);

        Call<JsonObject> call = accessTokenAPI.createAccessToken(user_name);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.d(MainChatActivity.TAG, "Response from server: " + response.body());

                TokenResponse tokenResponse = new TokenResponse();
                if (response.body() != null) {
                    tokenResponse.token = response.body().getAsJsonObject().getAsJsonObject("data").get("access_token").getAsString();
                    String accessToken = tokenResponse.token;
                    Log.d(MainChatActivity.TAG, "Retrieved access token from server: " + accessToken);
                    listener.receivedAccessToken(accessToken, null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(MainChatActivity.TAG, t.getLocalizedMessage());
                listener.receivedAccessToken(null, new Exception(t));
            }
        });
    }

    void sendMessage(String messageBody) {

        if (conversation != null) {
            Log.d(MainChatActivity.TAG, "Message created");
            conversation.prepareMessage()
                    .setBody(messageBody)
                    .buildAndSend(new CallbackListener<Message>() {
                        @Override
                        public void onSuccess(Message result) {
                            if (conversationsManagerListener != null) {
                                conversationsManagerListener.messageSentCallback();
                            }
                        }
                    });
        }
    }

    private void loadChannels() {
        if (conversationsClient == null || conversationsClient.getMyConversations() == null) {
            Log.d(MainChatActivity.TAG, "Test loadChannels");
            return;
        }
        conversationsClient.getConversation(DEFAULT_CONVERSATION_NAME, new CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) {
                    if (conversation.getStatus() == Conversation.ConversationStatus.JOINED
                            || conversation.getStatus() == Conversation.ConversationStatus.NOT_PARTICIPATING) {
                        Log.d(MainChatActivity.TAG, "Already Exists in Conversation: " + DEFAULT_CONVERSATION_NAME);
                        QuickstartConversationsManager.this.conversation = conversation;
                        QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                        QuickstartConversationsManager.this.loadPreviousMessages(conversation);
                    } else {
                        Log.d(MainChatActivity.TAG, "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                        joinConversation(conversation);
                    }
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(MainChatActivity.TAG, "Error retrieving conversation: " + errorInfo.getMessage());
                createConversation();
            }

        });
    }

    private void createConversation() {
        Log.d(MainChatActivity.TAG, "Creating Conversation: " + DEFAULT_CONVERSATION_NAME);

        conversationsClient.createConversation(DEFAULT_CONVERSATION_NAME,
                new CallbackListener<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        if (conversation != null) {
                            Log.d(MainChatActivity.TAG, "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                            joinConversation(conversation);
                        }
                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        Log.e(MainChatActivity.TAG, "Error creating conversation: " + errorInfo.getMessage());
                    }
                });
    }

    private void joinConversation(final Conversation conversation) {
        Log.d(MainChatActivity.TAG, "Joining Conversation: " + conversation.getUniqueName());
        if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {

            QuickstartConversationsManager.this.conversation = conversation;
            Log.d(MainChatActivity.TAG, "Already joined default conversation");
            QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
            return;
        }


        conversation.join(new StatusListener() {
            @Override
            public void onSuccess() {
                QuickstartConversationsManager.this.conversation = conversation;
                Log.d(MainChatActivity.TAG, "Joined default conversation");
                QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                QuickstartConversationsManager.this.loadPreviousMessages(conversation);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(MainChatActivity.TAG, "Error joining conversation: " + errorInfo.getMessage());
            }
        });
    }

    private void loadPreviousMessages(final Conversation conversation) {
        conversation.getLastMessages(100,
                new CallbackListener<List<Message>>() {
                    @Override
                    public void onSuccess(List<Message> result) {
                        messages.addAll(result);
                        if (conversationsManagerListener != null) {
                            conversationsManagerListener.reloadMessages();
                        }
                    }
                });
    }
}

