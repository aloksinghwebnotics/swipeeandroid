package com.webnotics.swipee.radarview;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.webnotics.swipee.R;
import com.webnotics.swipee.R.styleable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
        mv = {1, 5, 1},
        k = 1,
        d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\u0018\u00002\u00020\u0001:\u0001;B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0018\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u0002J\u0018\u0010!\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u0013H\u0002J(\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u0002J(\u0010*\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u0002J(\u0010+\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u0002J(\u0010,\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u0002J \u0010-\u001a\u00020$2\u0006\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u0002J\u001c\u0010.\u001a\u00020$2\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0002J\b\u0010/\u001a\u00020$H\u0002J\u0018\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u00072\u0006\u00102\u001a\u00020\u0007H\u0002J\u0018\u00103\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u00072\u0006\u00102\u001a\u00020\u0007H\u0002J\u0012\u00104\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0014J\u0018\u00105\u001a\u00020$2\u0006\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u00020\u0007H\u0014J\b\u00108\u001a\u00020$H\u0002J\u0006\u00109\u001a\u00020$J\u0006\u0010:\u001a\u00020$R\u000e\u0010\t\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082.¢\u0006\u0002\n\u0000R\u0018\u0010\u0018\u001a\f\u0012\b\u0012\u00060\u001aR\u00020\u00000\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0011X\u0082.¢\u0006\u0002\n\u0000¨\u0006<"},
        d2 = {"Lcom/webnotics/InstaCarriers/RadarView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "DEFAULT_COLOR", "isScanning", "", "isShowCrossLine", "isShowRaindrop", "mCircleColor", "mCircleNum", "mCirclePaint", "Landroid/graphics/Paint;", "mDegrees", "", "mFlicker", "mRaindropColor", "mRaindropNum", "mRaindropPaint", "mRaindrops", "Ljava/util/ArrayList;", "Lcom/webnotics/InstaCarriers/RadarView$Raindrop;", "mSpeed", "mSweepColor", "mSweepPaint", "changeAlpha", "color", "alpha", "dp2px", "dpVal", "drawCircle", "", "canvas", "Landroid/graphics/Canvas;", "cx", "cy", "radius", "drawCrossLine", "drawRaindrop", "drawSweep", "generateRaindrop", "initAttrs", "initPaints", "measureHeight", "measureSpec", "defaultSize", "measureWidth", "onDraw", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "removeRaindrop", "start", "stop", "Raindrop", "InstaCarriers.app"}
)
public final class Radar extends View {
    private  int DEFAULT_COLOR=Color.parseColor("#FF9800");
    private int mCircleColor;
    private int mCircleNum;
    private int mSweepColor;
    private int mRaindropColor;
    private int mRaindropNum;
    private boolean isShowCrossLine;
    private boolean isShowRaindrop;
    private float mSpeed;
    private float mFlicker;
    private Paint mCirclePaint;
    private Paint mSweepPaint;
    private Paint mRaindropPaint;
    private float mDegrees;
    private boolean isScanning;
    private  ArrayList mRaindrops=new ArrayList();

    private final void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            boolean var4 = false;
            boolean var5 = false;
            int var7 = 0;
            Intrinsics.checkNotNull(context);
            TypedArray var10000 = context.obtainStyledAttributes(attrs, styleable.RadarView);
            Intrinsics.checkNotNullExpressionValue(var10000, "context!!.obtainStyledAt…s, R.styleable.RadarView)");
            TypedArray mTypedArray = var10000;
            this.mCircleColor = mTypedArray.getColor(R.styleable.RadarView_kv_circleColor, this.DEFAULT_COLOR);
            this.mCircleNum = mTypedArray.getInt(R.styleable.RadarView_kv_circleNum, this.mCircleNum);
            if (this.mCircleNum < 1) {
                this.mCircleNum = 3;
            }

            this.mSweepColor = mTypedArray.getColor(R.styleable.RadarView_kv_sweepColor, this.DEFAULT_COLOR);
            this.mRaindropColor = mTypedArray.getColor(R.styleable.RadarView_kv_raindropColor, this.DEFAULT_COLOR);
            this.mRaindropNum = mTypedArray.getInt(R.styleable.RadarView_kv_raindropNum, this.mRaindropNum);
            this.isShowCrossLine = mTypedArray.getBoolean(R.styleable.RadarView_kv_showCrossLine, true);
            this.isShowRaindrop = mTypedArray.getBoolean(R.styleable.RadarView_kv_showRaindrop, true);
            this.mSpeed = mTypedArray.getFloat(R.styleable.RadarView_kv_speed, this.mSpeed);
            if (this.mSpeed <= (float)0) {
                this.mSpeed = 3.0F;
            }

            this.mFlicker = mTypedArray.getFloat(R.styleable.RadarView_kv_flicker, this.mFlicker);
            if (this.mFlicker <= (float)0) {
                this.mFlicker = 3.0F;
            }

            mTypedArray.recycle();
        }

    }

    private final void initPaints() {
        Paint var1 = new Paint();
        var1.setColor(this.mCircleColor);
        var1.setStrokeWidth(3.0F);
        var1.setStyle(Style.STROKE);
        var1.setAntiAlias(true);
        Unit var7 = Unit.INSTANCE;
        this.mCirclePaint = var1;
        var1 = new Paint();
        var1.setStyle(Style.FILL);
        var1.setAntiAlias(true);
        var7 = Unit.INSTANCE;
        this.mRaindropPaint = var1;
        var1 = new Paint();
        var1.setAntiAlias(true);
        var7 = Unit.INSTANCE;
        this.mSweepPaint = var1;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Context var10001 = this.getContext();
        Intrinsics.checkNotNullExpressionValue(var10001, "context");
        int defauleSize = this.dp2px(var10001, 200.0F);
        this.setMeasuredDimension(this.measureWidth(widthMeasureSpec, defauleSize), this.measureHeight(heightMeasureSpec, defauleSize));
    }

    private final int measureWidth(int measureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            result = defaultSize + this.getPaddingLeft() + this.getPaddingRight();
            if (specMode == Integer.MIN_VALUE) {
                boolean var6 = false;
                result = Math.min(result, specSize);
            }
        }

        int var9 = this.getSuggestedMinimumWidth();
        boolean var7 = false;
        result = Math.max(result, var9);
        return result;
    }

    private final int measureHeight(int measureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            result = specSize;
        } else {
            result = defaultSize + this.getPaddingTop() + this.getPaddingBottom();
            if (specMode == Integer.MIN_VALUE) {
                boolean var6 = false;
                result = Math.min(result, specSize);
            }
        }

        int var9 = this.getSuggestedMinimumHeight();
        boolean var7 = false;
        result = Math.max(result, var9);
        return result;
    }

    protected void onDraw(@Nullable Canvas canvas) {
        int mWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
        int mHeight = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
        boolean var5 = false;
        int radius = Math.min(mWidth, mHeight) / 2;
        int cx = this.getPaddingLeft() + (this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) / 2;
        int cy = this.getPaddingTop() + (this.getHeight() - this.getPaddingTop() - this.getPaddingBottom()) / 2;
        Intrinsics.checkNotNull(canvas);
        this.drawCircle(canvas, cx, cy, radius);
        if (this.isShowCrossLine) {
            this.drawCrossLine(canvas, cx, cy, radius);
        }

        if (this.isScanning) {
            if (this.isShowRaindrop) {
                this.drawRaindrop(canvas, cx, cy, radius);
            }

            this.drawSweep(canvas, cx, cy, radius);
            this.mDegrees = (this.mDegrees + 360.0F / this.mSpeed / 60.0F) % (float)360;
            this.invalidate();
        }

    }

    private final void drawCircle(Canvas canvas, int cx, int cy, int radius) {
        int i = 0;

        for(int var6 = this.mCircleNum; i < var6; ++i) {
            float var10001 = (float)cx;
            float var10002 = (float)cy;
            float var10003 = (float)(radius - radius / this.mCircleNum * i);
            Paint var10004 = this.mCirclePaint;
            if (var10004 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCirclePaint");
            }

            canvas.drawCircle(var10001, var10002, var10003, var10004);
        }

    }

    private final void drawCrossLine(Canvas canvas, int cx, int cy, int radius) {
        float var10001 = (float)(cx - radius);
        float var10002 = (float)cy;
        float var10003 = (float)(cx + radius);
        float var10004 = (float)cy;
        Paint var10005 = this.mCirclePaint;
        if (var10005 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCirclePaint");
        }

        canvas.drawLine(var10001, var10002, var10003, var10004, var10005);
        var10001 = (float)cx;
        var10002 = (float)(cy - radius);
        var10003 = (float)cx;
        var10004 = (float)(cy + radius);
        var10005 = this.mCirclePaint;
        if (var10005 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCirclePaint");
        }

        canvas.drawLine(var10001, var10002, var10003, var10004, var10005);
    }

    private final void drawRaindrop(Canvas canvas, int cx, int cy, int radius) {
        this.generateRaindrop(cx, cy, radius);
        Iterator var6 = this.mRaindrops.iterator();

        while(var6.hasNext()) {
           Radar.Raindrop raindrop = (Raindrop)var6.next();
            Paint var10000 = this.mRaindropPaint;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mRaindropPaint");
            }

            var10000.setColor(raindrop.changeAlpha$InstaCarriers_app());
            float var10001 = (float)raindrop.getX$InstaCarriers_app();
            float var10002 = (float)raindrop.getY$InstaCarriers_app();
            float var10003 = raindrop.getRadius$InstaCarriers_app();
            Paint var10004 = this.mRaindropPaint;
            if (var10004 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mRaindropPaint");
            }

            canvas.drawCircle(var10001, var10002, var10003, var10004);
            raindrop.setRadius$InstaCarriers_app(raindrop.getRadius$InstaCarriers_app() + 0.33333334F / this.mFlicker);
            raindrop.setAlpha$InstaCarriers_app(raindrop.getAlpha$InstaCarriers_app() - 4.25F / this.mFlicker);
        }

        this.removeRaindrop();
    }

    private final void drawSweep(Canvas canvas, int cx, int cy, int radius) {
        SweepGradient sweepGradient = new SweepGradient((float)cx, (float)cy, new int[]{0, this.changeAlpha(this.mSweepColor, 0), this.changeAlpha(this.mSweepColor, 180), this.changeAlpha(this.mSweepColor, 230), this.changeAlpha(this.mSweepColor, 255)}, new float[]{0.0F, 0.6F, 0.99F, 0.998F, 1.0F});
        Paint var10000 = this.mSweepPaint;
        if (var10000 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSweepPaint");
        }

        var10000.setShader((Shader)sweepGradient);
        canvas.rotate((float)-90 + this.mDegrees, (float)cx, (float)cy);
        float var10001 = (float)cx;
        float var10002 = (float)cy;
        float var10003 = (float)radius;
        Paint var10004 = this.mSweepPaint;
        if (var10004 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSweepPaint");
        }

        canvas.drawCircle(var10001, var10002, var10003, var10004);
    }

    private final void generateRaindrop(int cx, int cy, int radius) {
        if (this.mRaindrops.size() < this.mRaindropNum) {
            boolean probability = (int)(Math.random() * (double)20) == 0;
            if (probability) {
                int x = 0;
                int y = 0;
                int xOffset = (int)(Math.random() * (double)(radius - 20));
                double var10000 = Math.random();
                double var9 = 1.0D * (double)(radius - 20) * (double)(radius - 20) - (double)(xOffset * xOffset);
                boolean var11 = false;
                int yOffset = (int)(var10000 * (double)((int)Math.sqrt(var9)));

                if ((int)(Math.random() * (double)2) == 0) {
                    x = cx - xOffset;
                } else {
                    x = cx + xOffset;
                }

                if ((int)(Math.random() * (double)2) == 0) {
                    y = cy - yOffset;
                } else {
                    y = cy + yOffset;
                }

                this.mRaindrops.add(new Raindrop(x, y, 0.0F, this.mRaindropColor));
            }
        }

    }

    private final void removeRaindrop() {
        Iterator var10000 = this.mRaindrops.iterator();
        Intrinsics.checkNotNullExpressionValue(var10000, "mRaindrops.iterator()");
        Iterator iterator = var10000;

        while(true) {
            Raindrop raindrop;
            do {
                if (!iterator.hasNext()) {
                    return;
                }

                Object var3 = iterator.next();
                Intrinsics.checkNotNullExpressionValue(var3, "iterator.next()");
                raindrop = (Raindrop)var3;
            } while(!(raindrop.getRadius$InstaCarriers_app() > (float)20) && !(raindrop.getAlpha$InstaCarriers_app() < (float)0));

            iterator.remove();
        }
    }

    public final void start() {
        if (!this.isScanning) {
            this.isScanning = true;
            this.invalidate();
        }

    }

    public final void stop() {
        if (this.isScanning) {
            this.isScanning = false;
            this.mRaindrops.clear();
            this.mDegrees = 0.0F;
        }

    }

    private final int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private final int dp2px(Context context, float dpVal) {
        Resources var10002 = context.getResources();
        Intrinsics.checkNotNullExpressionValue(var10002, "context.resources");
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    @JvmOverloads
    public Radar(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Intrinsics.checkNotNullParameter(context, "context");
        this.DEFAULT_COLOR = Color.parseColor("#FF9800");
        this.mCircleColor = this.DEFAULT_COLOR;
        this.mCircleNum = 3;
        this.mSweepColor = this.DEFAULT_COLOR;
        this.mRaindropColor = this.DEFAULT_COLOR;
        this.mRaindropNum = 5;
        this.isShowCrossLine = true;
        this.isShowRaindrop = true;
        this.mSpeed = 3.0F;
        this.mFlicker = 3.0F;
        this.mRaindrops = new ArrayList();
        this.initAttrs(context, attrs);
        this.initPaints();
    }

    // $FF: synthetic method
    public Radar(Context var1, AttributeSet var2, int var3, int var4, DefaultConstructorMarker var5) {
        this(var1, var2, var3);
        if ((var4 & 2) != 0) {
            var2 = (AttributeSet)null;
        }

        if ((var4 & 4) != 0) {
            var3 = 0;
        }
    }

    @JvmOverloads
    public Radar(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 4, (DefaultConstructorMarker)null);
    }

    @JvmOverloads
    public Radar(@NotNull Context context) {
        this(context, (AttributeSet)null, 0, 6, (DefaultConstructorMarker)null);
    }

    @Metadata(
            mv = {1, 5, 1},
            k = 1,
            d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0014\b\u0082\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0002\u0010\bJ\r\u0010\u0018\u001a\u00020\u0003H\u0000¢\u0006\u0002\b\u0019R\u001a\u0010\t\u001a\u00020\u0006X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u0007\u001a\u00020\u0003X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0005\u001a\u00020\u0006X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001a\u0010\u0002\u001a\u00020\u0003X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001a\u0010\u0004\u001a\u00020\u0003X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011¨\u0006\u001a"},
            d2 = {"Lcom/webnotics/InstaCarriers/RadarView$Raindrop;", "", "x", "", "y", "radius", "", "color", "(Lcom/webnotics/InstaCarriers/Radar;IIFI)V", "alpha", "getAlpha$InstaCarriers_app", "()F", "setAlpha$InstaCarriers_app", "(F)V", "getColor$InstaCarriers_app", "()I", "setColor$InstaCarriers_app", "(I)V", "getRadius$InstaCarriers_app", "setRadius$InstaCarriers_app", "getX$InstaCarriers_app", "setX$InstaCarriers_app", "getY$InstaCarriers_app", "setY$InstaCarriers_app", "changeAlpha", "changeAlpha$InstaCarriers_app", "InstaCarriers.app"}
    )
    private final class Raindrop {
        private float alpha;
        private int x;
        private int y;
        private float radius;
        private int color;

        public final float getAlpha$InstaCarriers_app() {
            return this.alpha;
        }

        public final void setAlpha$InstaCarriers_app(float var1) {
            this.alpha = var1;
        }

        public final int changeAlpha$InstaCarriers_app() {
            int red = Color.red(this.color);
            int green = Color.green(this.color);
            int blue = Color.blue(this.color);
            return Color.argb((int)this.alpha, red, green, blue);
        }

        public final int getX$InstaCarriers_app() {
            return this.x;
        }

        public final void setX$InstaCarriers_app(int var1) {
            this.x = var1;
        }

        public final int getY$InstaCarriers_app() {
            return this.y;
        }

        public final void setY$InstaCarriers_app(int var1) {
            this.y = var1;
        }

        public final float getRadius$InstaCarriers_app() {
            return this.radius;
        }

        public final void setRadius$InstaCarriers_app(float var1) {
            this.radius = var1;
        }

        public final int getColor$InstaCarriers_app() {
            return this.color;
        }

        public final void setColor$InstaCarriers_app(int var1) {
            this.color = var1;
        }

        public Raindrop(int x, int y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.alpha = 255.0F;
        }
    }
}
