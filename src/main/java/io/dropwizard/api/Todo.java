package io.dropwizard.api;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class Todo {
	@NotNull
	private String id;

	@NotNull
	private String name;
	
	@NotNull
    private String description;
    
    private String date;

    private String status;

    private List<Todo> sub;
	private Tag tag;
	private String completionDate;
	
	public Todo() {
		
	}
	public Todo(String id, String name, String description,String date, String status, List<Todo> sub, Tag tag) {
		this.id = id;
		this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
        this.sub = sub;
        this.tag = tag;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
    }
    
    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public List<Todo> getSub(){
        return this.sub;
    }

    public void setSub(List<Todo> sub){
        this.sub = sub;
    }
	
	public Tag getTag() {
		return this.tag;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public void setCompletionDate(String date){
		this.completionDate = date;
	}

	public String getCompletionDate(){
		return this.completionDate;
	}
}