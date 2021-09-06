package com.kim.ilhwaland.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @JsonProperty  : 해당 어노테이션이 붙은 필드만, 출력, 입력 등의 JSON 관련 작업을 한다.
 * @JsonAutoDetect : JSON 매핑 법칙 변경(자동감지 설정) ( 필드나 메서드 위에 @JsonProperty 를 일일히 붙여주는 대신에 일괄처리 )
 * @JsonIgnoreProperties : JSON 입력 값에 대하여 Mapping 시 클래스에 선언되지 않은 property무시
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Carwash_post {
	
	@JsonProperty("title")  
	private String title;
	
	@JsonProperty("description") 
	private String description; // 설명
	
	@JsonProperty("bloggername") 
	private String bloggername; // 블로그 이름
	
	@JsonProperty("link") 
	private String link; 		// 블로그 링크

	public Carwash_post() {
	}
	
	// getter & setter
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBloggername() {
		return bloggername;
	}
	public void setBloggername(String bloggername) {
		this.bloggername = bloggername;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Carwash_blog [title=" + title + ", description=" + description + ", bloggername=" + bloggername
				+ ", link=" + link + "]";
	}
	
}
