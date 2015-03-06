package com.keyboard.bean;

public class EmoticonBean {

    public final static int FACE_TYPE_NOMAL = 0;
    public final static int FACE_TYPE_DEL = 1;
    public final static int FACE_TYPE_USERDEF = 2;

    /**
     * 点击处理事件类型
     */
    private long eventType;
    /**
     * 表情图标
     */
    private String iconUri;
    /**
     * 内容
     */
    private String content;

    public long getEventType() { return eventType; }

    public void setEventType(long eventType) { this.eventType = eventType; }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static String fromChars(String chars) { return chars; }

    public static String fromChar(char ch) { return Character.toString(ch); }

    public static String fromCodePoint(int codePoint) { return newString(codePoint); }

    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

    public EmoticonBean(long eventType , String iconUri , String content){
        this.eventType = eventType;
        this.iconUri = iconUri;
        this.content = content;
    }

    public EmoticonBean(){ }
}
