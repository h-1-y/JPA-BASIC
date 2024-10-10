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
			
			for ( int i=1; i<=20; i++ ) {
				
				Member member = new Member();
				
				member.setUsername("username" + i);
				member.setAge(i);
				member.setTeam(team);
				
				if ( i%2 == 0 ) member.setType(MemberType.USER);
				else member.setType(MemberType.ADMIN);
				
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
			
			em.flush();
			em.clear();
			
			// JPQL Join
			// inner join
			String joinQuery = "select m from Member m inner join m.team t";
			// left outer join
//			String joinQuery = "select m from Member m left outer join m.team t";
			// theta join
//			String joinQuery = "select m from Member m, Team t where m.username = t.name";
			List<Member> result = em.createQuery(joinQuery, Member.class).getResultList();
			
			em.flush();
			em.clear();
			
			// ON 절 사용
//			String onQuery = "select m from Member m left outer join m.team t on t.name = 'team A'";
			String onQuery = "select m from Member m left outer join Team t on m.username = t.name";
			List<Member> onResult = em.createQuery(onQuery, Member.class).getResultList();
			
			
			// JPQL SubQuery 서브쿼리
			// JPQL의 subquery는 where, having 절에서만 가능 hibernate는 select 절도 가능 FROM 절의 subquery는 JPQL에서는 불가능 X
			String subQuery = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
			List<Member> subResult = em.createQuery(subQuery, Member.class).getResultList();
			
			
			// JPQL 타입 표현
			// 문자 : 'Hello', 'She"s'
			// 숫자 : 10L(Long), 10D(Double), 10F(Float)
			// Boolean : TURE, FALSE
			// enum : hellojpa.MemberType (패키지명 포함)
			// Entity : TYPE(m) = Member (상속관계에서 사용)
//			String typeQuery = "select m.username, 'HELLO', true from Member m where m.type = hellojpa.MemberType.ADMIN";
			String typeQuery = "select m.username, 'HELLO', true from Member m where m.type = :userType";
			List<Object[]> typeResult = em.createQuery(typeQuery).setParameter("userType", MemberType.ADMIN).getResultList();
			
			for ( Object [] obj : typeResult ) {
				System.out.println("obj[0] == " + obj[0]);
				System.out.println("obj[1] == " + obj[1]);
				System.out.println("obj[2] == " + obj[2]);
			}
			
			
			// JPQL 조건식
			// case
			String caseQuery = "select " + 
							   "case when m.age <= 10 then '학생요금' " +
							   "when m.age >= 60 then '경로요금' " +
							   "else '일반요금' end " +
							   "from Member m";
			// coalesce
//			String caseQuery = "select coalesce(m.username, '이름 없는 회원') from Member m";
			// nullif
//			String caseQuery = "select nullif(m.username, 'admin') from Member m";
			
			List<String> caseResult = em.createQuery(caseQuery, String.class).getResultList();
			for ( String str : caseResult ) System.out.println(str);
			
			
			// JPQL 기본 함수
			// concat, substring, trim, lower, upper, length, locate, abs, sqrt, mod, size, index
			String basicQuery = "select concat('a', 'b') from Member m";
			
			
			// 경로 표현식
			// 단일 값 연관 경로
			// select m.team from Member e <- 묵시적 내부조인 발생 (Team을 가져오기 위해 Member와 Team을 조인한 쿼리가 나감)
			// 컬렉션 값 연관 경로
			// select t.members from Team t <- 묵시적 내부조인 발생 + 탐색 X 
			
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