package com.mc3699.ccdisplays.holoprojector.rendering;

public class HoloTextElement {

    private final float xPos;
    private final float yPos;
    private final float zPos;
    private final int color;
    private final float rotation;
    private final float scale;
    private final String text;

    public HoloTextElement(float x, float y, float z, float rotation, float scale, int color, String text)
    {
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
        this.color = color;
        this.text = text;
        this.scale = scale;
        this.rotation = rotation;
    }

    public String getText() {
        return text;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public float getZPos() {
        return zPos;
    }

    public int getColor() {
        return color;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }
}
