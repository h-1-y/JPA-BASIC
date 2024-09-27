package hellojpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import hellojpa.baseEntity.BaseEntity;

// 연관 관계 매핑 
@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	
	@Column(name = "MEMBER_NAME")
	private String name;
	
//	@Column(name = "TEAM_ID")
//	private Long teamId;
	
	// 양방향 연관관계 세팅 
	@ManyToOne(fetch = FetchType.LAZY) // N(Member) : 1(Team)
	@JoinColumn(name = "TEAM_ID") // join 컬럼을 명시 
	private Team team;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCKER_ID") // 일대일 관계 매핑 
	private Locker locker;
	
	@ManyToMany
	@JoinTable(name = "MEMBER_PRODUCT")
	private List<Product> products = new ArrayList<>();
	
	// 근무 기간 
	@Embedded
	private Period workPeriod;
	
	// 주소
	@Embedded
	private Address homeAddress;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY"))
		, @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET"))
		, @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
	})
	private Address workAddress;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void changeTeam(Team team) {
		this.team = team;
		
		// 양방향 연관관계를 위한 편의 메소드
		team.getMembers().add(this);
		
	}

	public Locker getLocker() {
		return locker;
	}

	public void setLocker(Locker locker) {
		this.locker = locker;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Period getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Period workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Address getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(Address workAddress) {
		this.workAddress = workAddress;
	}
	
}
