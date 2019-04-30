package per.zdy.socketexchangeclientcp.domain.Pojo;

import javax.persistence.*;

@Entity
@Table(name = "passList")
public class PassList {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    String agent;

    @Column(nullable = false)
    String remoteAdd;

    @Column(nullable = false)
    String remotePort;

    @Column(nullable = false)
    String type;

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getRemoteAdd() {
        return remoteAdd;
    }

    public void setRemoteAdd(String remoteAdd) {
        this.remoteAdd = remoteAdd;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PassList{" +
                "agent=" + agent +
                ", remoteAdd='" + remoteAdd + '\'' +
                ", remotePort='" + remotePort + '\'' +
                ", type=" + type +
                '}';
    }
}
