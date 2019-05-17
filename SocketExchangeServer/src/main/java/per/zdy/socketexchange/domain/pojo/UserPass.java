package per.zdy.socketexchange.domain.pojo;

import javax.persistence.*;

@Entity
@Table(name = "userPass")
public class UserPass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //??
    @Id
    private Long id;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String INHERIT;

    @Column(nullable = false)
    String IP = "";

    @Column(nullable = false)
    String PORT ;

    @Column(nullable = false)
    String TYPE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getINHERIT() {
        return INHERIT;
    }

    public void setINHERIT(String INHERIT) {
        this.INHERIT = INHERIT;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    @Override
    public String toString() {
        return "PassList{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", INHERIT='" + INHERIT + '\'' +
                ", IP='" + IP + '\'' +
                ", PORT='" + PORT + '\'' +
                ", TYPE='" + TYPE + '\'' +
                '}';
    }
}
