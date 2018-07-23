package com.ibasco.pidisplay.core.gpio;

public class GpioEvent {

    private int msg;

    private int type;

    private int value;

    private String desc;

    public GpioEvent(int msg, int type, int value, String desc) {
        this.msg = msg;
        this.type = type;
        this.value = value;
        this.desc = desc;
    }

    public int getMsg() {
        return msg;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GpioEvent{");
        sb.append("msg=").append(msg);
        sb.append(", type=").append(type);
        sb.append(", value=").append(value);
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
