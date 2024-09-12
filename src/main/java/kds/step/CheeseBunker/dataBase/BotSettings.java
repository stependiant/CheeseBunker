package kds.step.CheeseBunker.dataBase;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BotSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logChannelId;
    private String supportRoleId;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(String logChannelId) {
        this.logChannelId = logChannelId;
    }

    public String getSupportRoleId() {
        return supportRoleId;
    }

    public void setSupportRoleId(String supportRoleId) {
        this.supportRoleId = supportRoleId;
    }
}