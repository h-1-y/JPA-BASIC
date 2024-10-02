package hellojpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import hellojpa.item.Album;
import hellojpa.item.Item;
import hellojpa.item.Movie;

public class JpaMain {

	public static void main(String[] args) {
		
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction et = em.getTransaction();
		
		et.begin();
		
		try {
			
			// JPQL
			List<Member> members = em.createQuery("select m from Member m where m.name like '%kim%'", Member.class).getResultList();
			
			// JAVA 표준 스펙 Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Member> query = cb.createQuery(Member.class);
			
			Root<Member> m = query.from(Member.class);
			
			CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
			List<Member> resultList = em.createQuery(cq).getResultList();
			
		} catch (Exception e) {
			 
			et.rollback();
			e.printStackTrace(); 
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}