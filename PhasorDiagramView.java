package com.android.phasordiagramtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class PhasorDiagramView extends View {

    private float voltageRMagnitude;
    private float voltageYMagnitude;
    private float voltageBMagnitude;
    private float currentRMagnitude;
    private float currentYMagnitude;
    private float currentBMagnitude;
    private Paint paint;

    public PhasorDiagramView(Context context) {
        super(context);
        init();
    }

    public PhasorDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float radius = Math.min(centerX, centerY) * 0.75f;

        // Draw axes
        canvas.drawLine(0, centerY, getWidth(), centerY, paint);
        canvas.drawLine(centerX, 0, centerX, getHeight(), paint);

        // Draw voltage phasors (120 degrees apart)
        drawPhasor(canvas, centerX, centerY, radius, voltageRMagnitude, 0, "VR",false);
        drawPhasor(canvas, centerX, centerY, radius, voltageYMagnitude, -120, "VY",false);
        drawPhasor(canvas, centerX, centerY, radius, voltageBMagnitude, 120, "VB",false);

        // Draw current phasors (centered, same angle as voltages)
        drawPhasor(canvas, centerX, centerY, radius / 2, currentRMagnitude, 0, "IR", true);
        drawPhasor(canvas, centerX, centerY, radius / 2, currentYMagnitude, -120, "IY", true);
        drawPhasor(canvas, centerX, centerY, radius / 2, currentBMagnitude, 120, "IB", true);

        // Draw neutral point
        canvas.drawCircle(centerX, centerY, 5, paint);
    }

    private void drawPhasor(Canvas canvas, int centerX, int centerY, float radius,
                            float magnitude, float angle, String label, boolean isCurrent) {
        float x = (float) (radius * Math.cos(Math.toRadians(angle)));
        float y = (float) (radius * Math.sin(Math.toRadians(angle)));

        // Adjust position for currents (centered)
        if (isCurrent) {
            x += centerX;
            y = centerY;
        }

        Path path = new Path();
        path.moveTo(centerX, centerY);
        path.lineTo(centerX + x, centerY - y);
        canvas.drawPath(path, paint);

        // Draw label with appropriate offset
        float offsetX = isCurrent ? 10 : (x > 0 ? -10 : 20);
        canvas.drawText(label + " (" + magnitude + "∠" + angle + "°)", centerX + x + offsetX, centerY - y - 5, paint);
    }

    public void setVoltageMagnitudes(float voltageRMagnitude, float voltageYMagnitude, float voltageBMagnitude) {
        this.voltageRMagnitude = voltageRMagnitude;
        this.voltageYMagnitude = voltageYMagnitude;
        this.voltageBMagnitude = voltageBMagnitude;
        invalidate();
    }

    public void setCurrentMagnitudes(float currentRMagnitude, float currentYMagnitude, float currentBMagnitude) {
        this.currentRMagnitude = currentRMagnitude;
        this.currentYMagnitude = currentYMagnitude;
        this.currentBMagnitude = currentBMagnitude;
        invalidate();
    }
}

