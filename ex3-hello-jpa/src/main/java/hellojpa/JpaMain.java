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
			
			for ( int i=1; i<=100; i++ ) {
				
				Member member = new Member();
				
				member.setUsername("username" + i);
				member.setAge(i);
				member.setTeam(team);
				
				em.persist(member);
				
			}
			
			// JPQL ( Java Persistence Query Language )
			
			// TypeQuery, Query
			// TypeQyery : 반환 타입이 명확할 때 사용 
			TypedQuery<Member> typedQuery = em.createQuery("select m from Member m", Member.class);
			
			// Query : 반환 타입이 명확하지 않을 때 사용 -> m.username은 String, m.age는 int 
			// 쿼리 결과의 반환 타입이 명확하지 않을 때 사용
			Query query = em.createQuery("select m.username, m.age from Member m");
			
			// 파라미터 바인딩 : <- 사용
			Member members = em.createQuery("select m from Member m where m.username = :username", Member.class)
									.setParameter("username", "username1")
									.getSingleResult(); // SingleResult 사용시 값이 없거나 1개 이상이면 Exception
			
			
			// 프로젝션 = select 절에서 조회할 대상을 지정하는 것 
			// 엔티티 프로젝션 
			em.createQuery("select m from Member m", Member.class);
			em.createQuery("select m.team from Member m", Team.class);
			
			// 임베디드 타입 프로젝션 
			em.createQuery("select o.address from Order o", Address.class);
			
			// 스칼라 타입 프로젝션
			em.createQuery("select m.username, m.age from Member m");
			
			// 프로젝션 여러 값 조회 - Query, Object[], new 명령어
			// Query 타입으로 조회 
			Query query1 = em.createQuery("select m.username, m.age from Member m");
			
			// Objec[] 타입으로 조회
			List<Object[]> objectList = em.createQuery("select m.username, m.age from Member m").getResultList();
			
			Object [] o = objectList.get(0);
			
			System.out.println("o[0] ==== " + o[0]);
			System.out.println("o[1] ==== " + o[1]);
			
			// new 명령어로 조회 
			// 단순 값을 DTO로 바로 조회, 패키지 명을 포함한 전체 클래스명 입력, 순서와 타입이 일치하는 생성자 필요
			List<MemberDTO> resultList = em.createQuery("select new hellojpa.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();
			
			// JPQL Paging 처리 
			List<Member> pagingMembers = em.createQuery("select m from Member m order by m.age desc", Member.class)
						.setFirstResult(0) // 0번째부터 
						.setMaxResults(10) // 10개 가져온다
						.getResultList();
			
			for ( Member m : pagingMembers ) System.out.println(m.toString());
			
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