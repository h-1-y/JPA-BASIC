package hellojpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class JpaMain {

	public static void main(String[] args) {
		
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try {
			
			Team team = new Team();
			
			team.setName("team A");
			
			em.persist(team);
			
			Member member = new Member();
			
			member.setUsername("my name");
			member.setAge(20);
			member.setTeam(team);
			
			em.persist(member);
			
			// JPQL ( Java Persistence Query Language )
			
			// TypeQuery, Query
			// TypeQyery : 반환 타입이 명확할 때 사용 
			TypedQuery<Member> typedQuery = em.createQuery("select m from Member m", Member.class);
			
			// Query : 반환 타입이 명확하지 않을 때 사용 -> m.username은 String, m.age는 int 
			// 쿼리 결과의 반환 타입이 명확하지 않을 때 사용
			Query query = em.createQuery("select m.username, m.age from Member m");
			
			// 파라미터 바인딩 : <- 사용
			Member members = em.createQuery("select m from Member m where m.username = :username", Member.class)
									.setParameter("username", "my name")
									.getSingleResult();
			
			System.out.println("member.getUsername() === " + member.getUsername());
			
			et.commit();
			
		} catch (Exception e) {
			 
			et.rollback();
			e.printStackTrace(); 
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}