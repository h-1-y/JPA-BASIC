package hellojpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
//@Table(name = "USER") 테이블명 지정 방법 
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
public class Member {

	@Id // PK 설정
	@GeneratedValue(strategy = GenerationType.SEQUENCE // 기본 키 매핑 전략 ( IDENTITY, SEQUENCE, TABLE, AUTO )
					, generator = "member_seq_generator") // 시퀀스 전략 ( entity에 정의해둔 seq name mapping )
	private long id;
	
	@Column(name = "name", length = 2000) // 컬럼명 지정 방법
	private String username;

	private Integer age;
	
	@Enumerated(EnumType.STRING) // Enum 타입을 쓰는 경우 
	private RoleType roleType;
	
	@Temporal(TemporalType.TIMESTAMP) // Date 타입을 쓰는 경우 ( DATE, TIME, TIMESTAMP 3가지 )
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastMoidfyDate;
	
	@Lob // Clob, Blob 등등 
	private String description;
	
	@Transient // table과 매핑하지 않고 사용하기 위한 선언
	private int temp;
	
	public Member() {}

	public Member(long id, String username, Integer age, RoleType roleType, Date createDate, Date lastMoidfyDate,
			String description) {
		super();
		this.id = id;
		this.username = username;
		this.age = age;
		this.roleType = roleType;
		this.createDate = createDate;
		this.lastMoidfyDate = lastMoidfyDate;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastMoidfyDate() {
		return lastMoidfyDate;
	}

	public void setLastMoidfyDate(Date lastMoidfyDate) {
		this.lastMoidfyDate = lastMoidfyDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
