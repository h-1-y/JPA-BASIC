package hellojpa.baseEntity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass // entity의 공통 컬럼 속성을 사용하기 위한 편의? 속성
// 직접 생성해서 사용할일이 없기때문에 추상클래스로 선언 후 사용 권장 
public abstract class BaseEntity {

	@Column(name = "IN_USER")
	private String inUser;
	
	@Column(name = "IN_DATE")
	private LocalDateTime inDate;
	
	@Column(name = "UP_USER")
	private String upUser;
	
	@Column(name = "UP_DATE")
	private LocalDateTime upDate;
	
	public String getInUser() {
		return inUser;
	}
	public void setInUser(String inUser) {
		this.inUser = inUser;
	}
	public LocalDateTime getInDate() {
		return inDate;
	}
	public void setInDate(LocalDateTime inDate) {
		this.inDate = inDate;
	}
	public String getUpUser() {
		return upUser;
	}
	public void setUpUser(String upUser) {
		this.upUser = upUser;
	}
	public LocalDateTime getUpDate() {
		return upDate;
	}
	public void setUpDate(LocalDateTime upDate) {
		this.upDate = upDate;
	}
	
}
