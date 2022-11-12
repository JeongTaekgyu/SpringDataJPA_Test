package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 속성들을 상속하게 해준다.
@Getter
public class JpaBaseEntity { // 순수 jpa로 Auditing을 해결

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    @PrePersist // Persist 하기 전에 이벤트가 발생한다.
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updateDate = now;
    }

    @PreUpdate // update하기 전에 호출된다.
    public void preUpdate(){
        this.updateDate = LocalDateTime.now();
    }
}
