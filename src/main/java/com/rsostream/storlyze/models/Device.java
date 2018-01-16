package com.rsostream.storlyze.models;

import org.bson.types.ObjectId;

public final class Device {
    private ObjectId _id;
    private String imei;

    public Device() {
    }

    public Device(ObjectId id, String imei) {
        this._id = id;
        this.imei = imei;
    }

    public Device(String imei) {
        this.imei = imei;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "imei = " + this.imei;
    }
}
