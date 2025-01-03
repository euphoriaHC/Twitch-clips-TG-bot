package com.alexlatkin.twitchclipstgbot.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "broadcaster")
public class Broadcaster {
    @Id
    @Column(name = "broadcaster_id")
    private Integer broadcasterId;
    @Column(name = "broadcaster_name")
    private String broadcasterName;
    @ManyToMany(mappedBy = "followList", fetch = FetchType.EAGER)
    private List<User> userFollowList;
    @ManyToMany(mappedBy = "blackList", fetch = FetchType.EAGER)
    private List<User> userBlackList;

    public Broadcaster(Integer broadcasterId, String broadcasterName) {
        this.broadcasterId = broadcasterId;
        this.broadcasterName = broadcasterName;
    }

    @Override
    public String toString() {
        return "Broadcaster{" +
                "broadcasterId=" + broadcasterId +
                ", broadcasterName='" + broadcasterName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Broadcaster that = (Broadcaster) o;
        return broadcasterId.equals(that.broadcasterId) && broadcasterName.equals(that.broadcasterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(broadcasterId, broadcasterName);
    }
}
