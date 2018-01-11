package com.rsostream.storlyze.models.device;

import org.bson.types.ObjectId;

public final class Device {
    private ObjectId _id;
    private String IMEI;

    public Device() {

    }

    public Device(ObjectId id, String IMEI) {
        this._id = id;
        this.IMEI = IMEI;
    }

    public Device(String IMEI) {
        this.IMEI = IMEI;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    @Override
    public String toString() {
        return "IMEI = " + this.IMEI;
    }
}
