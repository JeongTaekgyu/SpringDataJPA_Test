package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass // 속성들을 상속하게 해준다.
@Getter
public class BaseEntity extends BaseTimeEntity{

    @CreatedBy // 생성자
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy // 수정자
    private String lastModifiedBy;
}
