package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){ // 전체조회
        // JPQL은 객체를 대상으로 하는 쿼리이다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id){ // id로 하나 조회
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThanWithJpql(String usernme, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", usernme)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) // offset 부터 가져오겠다.
                .setMaxResults(limit) // limit 개수만큼 가져오겟다.
                .getResultList();    // 리스트로 넘긴다.
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult(); // SingleResult로 넘긴다.
    }

    public int bulkAgePlus(int age) {
        return em.createQuery(
                "update Member m set m.age = m.age + " +
                        "1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
