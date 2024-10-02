package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import hellojpa.baseEntity.BaseEntity;

@Entity
public class Team extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "TEAM_ID")
	private Long id;
	
	private String name;
	
	// 양방향 연관관계 세팅 
	@OneToMany(mappedBy = "team") // 1(Team) : N(Member) 
	// mappedBy - 어떤 객체와 연관 되어있는지 작성 ( 연관관계의 주인이 아닌경우 mappedBy 지정 / 주인이 아닌쪽은 읽기만 가능 )
	private List<Member> members = new ArrayList<>();

	public void addMember(Member member) {
		member.setTeam(this);
		members.add(member);
	}
	
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

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
}
