package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		
		// EntityManagerFactory는 하나만 생성해서 애플리케이션 전체에서 공유 
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		// EntityManager는 스레드간 공유 X(사용하고 버려야 함)
		EntityManager em = emf.createEntityManager();
		
//		EntityTransaction et = em.getTransaction();
//		et.begin();
		
		em.getTransaction().begin();
		
		try {
			
			// 등록 
			Member member = new Member();
			
			member.setId(1L);
			member.setName("HAN");
			
			Member member2 = new Member();
			
			member2.setId(2L);
			member2.setName("PARK");
			
			em.persist(member);
			em.persist(member2);
//			
//			em.getTransaction().commit();
			
			// 조회 
			Member findMember = em.find(Member.class, 1L);
			
			System.out.println(findMember.getId());
			System.out.println(findMember.getName());
			
			// 수정 
			findMember.setName("KIM");
//			em.persist(findMember); <- 안해도 됨
			// set으로 값만 바꿨을 뿐인데 update 쿼리문이 실행되는 이유는 JPA를 통해서 entity를 가져오면(find) 해당 entity를 JPA에서 관리한다.
			// JPA가 데이터가 변경 됐는지 안됐는지(변경 감지) transaction을 commit하는 시점에 체크를 한다. 
			// 그래서 변경이 감지되면 JPA에서 update 쿼리문을 날린다. 
			
			
			
			// JPQL = Table이 아닌 Entity(객체)를 대상으로 검색하는 객체 지향 쿼리이다.
			// 쿼리문의 Member는 테이블이 대상이 아니라 Entity = Member가 대상이다. 즉, 객체가 대상
			// JPA를 사용하면 엔티티(객체)를 중심으로 개발할 수 있지만 검색 쿼리를 만드는데 문제가 있다.
			// 검색을 할 때도 Table이 아닌 Entity(객체)를 대상이기 때문에 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능에 가깝다.
			// 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요하다. 
			// 그래서 JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공한다.
			// SQL 문법과 유사( SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 등 지원 )
			List<Member> findMembers = em.createQuery("select m from Member as m", Member.class).getResultList();
			
			System.out.println(findMembers.size());

			em.getTransaction().commit();
			
			
			// 삭제 
//			em.remove(findMember);
			
		} catch (Exception e) {
			
			em.getTransaction().rollback();
			
		} finally {
			
			em.close();
			emf.close();
			
		}
		
	}
	
}