package per.zdy.socketexchangeclientcp.domain.Pojo;

import javax.persistence.*;

@Entity
@Table(name = "passList")
public class PassList {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //??
    @Id
    private Long id;

    @Column(nullable = false)
    String agentAdd;

    @Column(nullable = false)
    String agentPort;

    @Column(nullable = false)
    String remoteAdd;

    @Column(nullable = false)
    String remotePort;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    int deleteFlag=0;

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentAdd() {
        return agentAdd;
    }

    public void setAgentAdd(String agentAdd) {
        this.agentAdd = agentAdd;
    }

    public String getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(String agentPort) {
        this.agentPort = agentPort;
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
                "id=" + id +
                ", agentAdd='" + agentAdd + '\'' +
                ", agentPort='" + agentPort + '\'' +
                ", remoteAdd='" + remoteAdd + '\'' +
                ", remotePort='" + remotePort + '\'' +
                ", type=" + type +
                '}';
    }
}
