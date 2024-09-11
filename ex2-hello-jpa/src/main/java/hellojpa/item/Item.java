package hellojpa.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
// 조인 전략 - default는 싱글테이블(상속관계를 전부 한테이블에)
//@Inheritance(strategy = InheritanceType.JOINED)
// 단인 테이블 전략 - @DiscriminatorColumn 옵션을 넣지않아도 자동으로 생성
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 구현 클레스마다 테이블 전략 - 부모의 컬럼을 하위의 테이블들이 모두 갖는 형태
// abstract 추상 클래스로 사용해야함 / item 테이블은 생성 X
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// DTYPE <- 자식 타입 명시 컬럼..?
// name 속성 default는 DTYPE
@DiscriminatorColumn(name = "DATA_TYPE") 
public abstract class Item {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private int price;

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
