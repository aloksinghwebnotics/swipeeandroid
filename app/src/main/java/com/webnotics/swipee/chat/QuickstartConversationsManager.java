package com.webnotics.swipee.chat;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.ErrorInfo;
import com.twilio.conversations.MediaUploadListener;
import com.twilio.conversations.Message;
import com.twilio.conversations.Participant;
import com.twilio.conversations.StatusListener;
import com.twilio.conversations.User;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.SwipeeApiClient;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


public class QuickstartConversationsManager {

    // This is the unique name of the conversation  we are using
    private  static String DEFAULT_CONVERSATION_NAME = "";

    final private ArrayList<Message> messages = new ArrayList<>();

    private ConversationsClient conversationsClient;

    private Conversation conversation;

    private QuickstartConversationsManagerListener conversationsManagerListener;

    private String tokenURL = "";
    private String user_name = "";
    private String appointment_id = "";
    private String appointment_number = "";

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
                    AppController.dismissProgressdialog();
                    Log.e(MainChatActivity.TAG, "Error creating Twilio Conversations Client: " + errorInfo.getMessage());
                }
            };


    private final ConversationListener mDefaultConversationListener = new ConversationListener() {


        @Override
        public void onMessageAdded(final Message message) {
            Log.d(MainChatActivity.TAG, "Message added");

            if (conversationsManagerListener != null) {
                messages.add(message);
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

    void retrieveAccessTokenFromServer(final Context context, String identity, String appointmentId, String appointmentNumber,
                                       final TokenResponseListener listener) {

        // Set the chat token URL in your strings.xml file
        String chatTokenURL = context.getString(R.string.chat_token_url);

        if ("https://YOUR_DOMAIN_HERE.twil.io/chat-token".equals(chatTokenURL)) {
            AppController.dismissProgressdialog();
            listener.receivedTokenResponse(false, new Exception("You need to replace the chat token URL in strings.xml"));
            return;
        }

        // tokenURL = chatTokenURL + "?user_name=" + identity;
        user_name = identity;
        tokenURL = chatTokenURL;
        appointment_id = appointmentId;
        appointment_number = appointmentNumber;

        new Thread(() -> retrieveToken((token, exception) -> {
            if (token != null) {
                ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
                ConversationsClient.create(context, token, props, mConversationsClientCallback);
                listener.receivedTokenResponse(true, null);
            } else {
                listener.receivedTokenResponse(false, exception);
            }
        })).start();
    }

    void initializeWithAccessToken(final Context context, final String token) {

        ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
        ConversationsClient.create(context, token, props, mConversationsClientCallback);
    }

    private void retrieveToken(AccessTokenListener listener) {

        SwipeeApiClient.swipeeServiceInstance().createAccessToken(Config.GetUserToken(),user_name,appointment_id,appointment_number).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.d(MainChatActivity.TAG, "Response from server: " + response.body());
                TokenResponse tokenResponse = new TokenResponse();
                if (response.body() != null) {
                    if (response.body().getAsJsonObject().has("data")){
                        if (response.body().getAsJsonObject().getAsJsonObject("data").has("channel_name")){
                            DEFAULT_CONVERSATION_NAME = response.body().getAsJsonObject().getAsJsonObject("data").get("channel_name").getAsString();
                            if (Config.isSeeker()){
                                tokenResponse.token = response.body().getAsJsonObject().getAsJsonObject("data").get("user_access_token").getAsString();
                            }else {
                                tokenResponse.token = response.body().getAsJsonObject().getAsJsonObject("data").get("company_access_token").getAsString();
                            }
                            String accessToken = tokenResponse.token;
                            Log.d(MainChatActivity.TAG, "Retrieved access token from server: " + accessToken);
                            listener.receivedAccessToken(accessToken, null);
                        }else AppController.dismissProgressdialog();

                    }else AppController.dismissProgressdialog();

                }else AppController.dismissProgressdialog();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
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


    void sendMedia(InputStream inputStream,String contentType,String fileName, MediaUploadListener uploadListener) {

        if (conversation != null) {
            Log.d(MainChatActivity.TAG, "Message created");
            conversation.prepareMessage()
                    .addMedia(inputStream,contentType,fileName,uploadListener)
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
            AppController.dismissProgressdialog();
            return;
        }
        conversationsClient.getConversation(DEFAULT_CONVERSATION_NAME, new CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) {
                    if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {
                        Log.d(MainChatActivity.TAG, "Already Exists in Conversation: " + DEFAULT_CONVERSATION_NAME);
                        Log.d(MainChatActivity.TAG, "SID: " + conversation.getSid());
                        Log.d(MainChatActivity.TAG, "SID: " + conversation);
                        QuickstartConversationsManager.this.conversation = conversation;
                        QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                        QuickstartConversationsManager.this.loadPreviousMessages(conversation);
                        AppController.dismissProgressdialog();
                    } else {
                      conversation.setUniqueName(DEFAULT_CONVERSATION_NAME, new StatusListener() {
                          @Override
                          public void onSuccess() {
                              Log.d(MainChatActivity.TAG, "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                              joinConversation(conversation);
                          }

                          @Override
                          public void onError(ErrorInfo errorInfo) {
                              AppController.dismissProgressdialog();
                              StatusListener.super.onError(errorInfo);
                          }
                      });
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
                        conversation.setUniqueName(DEFAULT_CONVERSATION_NAME, new StatusListener() {
                            @Override
                            public void onSuccess() {
                                if (conversation != null) {
                                    Log.d(MainChatActivity.TAG, "Joining Conversation: " + DEFAULT_CONVERSATION_NAME);
                                    joinConversation(conversation);
                                }else AppController.dismissProgressdialog();
                            }
                            @Override
                            public void onError(ErrorInfo errorInfo) {
                                StatusListener.super.onError(errorInfo);
                                AppController.dismissProgressdialog();
                            }
                        });

                    }

                    @Override
                    public void onError(ErrorInfo errorInfo) {
                        AppController.dismissProgressdialog();
                        Log.e(MainChatActivity.TAG, "Error creating conversation: " + errorInfo.getMessage());
                    }
                });
    }

    private void  joinConversation(final Conversation conversation) {
        Log.d(MainChatActivity.TAG, "Joining Conversation: " + conversation.getUniqueName());
        if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {
            QuickstartConversationsManager.this.conversation = conversation;
            Log.d(MainChatActivity.TAG, "Already joined default conversation");
            QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
            AppController.dismissProgressdialog();
            return;
        }


        conversation.join(new StatusListener() {
            @Override
            public void onSuccess() {
                QuickstartConversationsManager.this.conversation = conversation;
                Log.d(MainChatActivity.TAG, "Joined default conversation");
                QuickstartConversationsManager.this.conversation.addListener(mDefaultConversationListener);
                QuickstartConversationsManager.this.loadPreviousMessages(conversation);
                AppController.dismissProgressdialog();
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                AppController.dismissProgressdialog();
                Log.e(MainChatActivity.TAG, "Error joining conversation: " + errorInfo.getMessage());
            }
        });
    }

    private void loadPreviousMessages(final Conversation conversation) {
        conversation.getLastMessages(500,
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

