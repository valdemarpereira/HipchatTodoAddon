package com.valdemar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Installable {

    private String oauthId;
    private String capabilitiesUrl;
    private Integer roomId;
    private Integer groupId;
    private String oauthSecret;

    public String getOauthId() {
        return oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    public String getCapabilitiesUrl() {
        return capabilitiesUrl;
    }

    public void setCapabilitiesUrl(String capabilitiesUrl) {
        this.capabilitiesUrl = capabilitiesUrl;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }

    @Override
    public String toString() {
        return "Installable{" +
                "oauthId='" + oauthId + '\'' +
                ", capabilitiesUrl='" + capabilitiesUrl + '\'' +
                ", roomId=" + roomId +
                ", groupId=" + groupId +
                ", oauthSecret='" + oauthSecret + '\'' +
                '}';
    }
}
