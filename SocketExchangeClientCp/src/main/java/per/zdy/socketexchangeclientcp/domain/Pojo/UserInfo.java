package per.zdy.socketexchangeclientcp.domain.Pojo;

import javax.persistence.*;

@Entity
@Table(name = "userInfo")
public class UserInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //??
    @Id
    private Long id;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String userPwd;

    @Column(nullable = false)
    String userName = "";

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

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "PassList{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
