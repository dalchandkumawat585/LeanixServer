package io.dropwizard.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.dropwizard.api.Todo;
import io.dropwizard.api.Tag;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {

    private static List<Todo> todos = new ArrayList<>();
    private static List<Tag> tags = new ArrayList<>();

	@GET
	public List<Todo> getTodos() {
		return TodoResource.todos;
	}

	@POST
	public Todo addTodos(Todo todo) {
		String id = UUID.randomUUID().toString();
		todo.setId(id);
        todo.getSub().forEach(sub -> sub.setId(UUID.randomUUID().toString()));
        if(todo.getDate()==null){
            LocalDate d1 = null;
            for(Todo sub: todo.getSub()){
                if(sub.getDate()!=null){
                    String[] date = sub.getDate().split("-");
                    LocalDate d2 = LocalDate.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
                    if(d1==null || d2.isAfter(d1))
                        d1 = d2;
                }
            }
            if(todo.getSub().size()>0){                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                if(d1!=null)
                    todo.setDate(d1.format(formatter));
            }
        }
        if(todo.getStatus().equals("done")){
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
            String strDate = formatter.format(date); 
            todo.setCompletionDate(strDate);
        }
        TodoResource.todos.add(todo);
		return todo;
	}

	@GET
	@Path("{id}")
	public Todo getTodo(@PathParam("id") String id) {
		Optional<Todo> findFirst = TodoResource.todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
		if(!findFirst.isPresent()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);		
		}
		return findFirst.get();
	}

	@PUT
	@Path("{id}")
	public Response updateTodo(@PathParam("id") String id, Todo todoParam) {
		Optional<Todo> findFirst = TodoResource.todos.stream().filter(todo -> todo.getId().equals(id)).findFirst();
		if(!findFirst.isPresent()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		Todo todo = findFirst.get();
		todo.setName(todoParam.getName());
		todo.setDescription(todoParam.getDescription());
        todo.setTag(todoParam.getTag());
        todo.setStatus(todoParam.getStatus());
		deleteTodo(id);
		TodoResource.todos.add(todo);
		return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteTodo(@PathParam("id") String id) {
		TodoResource.todos = TodoResource.todos.stream().filter(todo -> !todo.getId().equals(id)).collect(Collectors.toList());
		return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/tags")
    public List<Tag> getTags(){
        return tags;
    }

    @POST
    @Path("/tags")
    public Tag addTag(Tag tag){
        String id = UUID.randomUUID().toString();
		tag.setId(id);
        tags.add(tag);
        return tag;
    }

    @POST
    @Path("/assigntag")
    public Response assignTag(@QueryParam("id") String id, Todo todo){
        Optional<Tag> findFirst = TodoResource.tags.stream().filter(tag -> tag.getId().equals(id)).findFirst();
		if(!findFirst.isPresent()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		Tag tag = findFirst.get();
        todo.setTag(tag);
        Todo temp = todo;
        deleteTodo(todo.getId());
        TodoResource.todos.add(temp);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/markcomplete")
    public Response markComplete(Todo todo){
        Todo temp = todo;
        temp.setStatus("done");
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = formatter.format(date); 
        temp.setCompletionDate(strDate);
        deleteTodo(todo.getId());
		TodoResource.todos.add(temp);
		return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("updatestatus")
    public Response updateStatus(){
        List<Todo> temp = new ArrayList<>();
        for(Todo todo: TodoResource.todos){
            if(todo.getStatus().equals("pending")){
                Date date = new Date();  
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
                String strDate = formatter.format(date);
                String[] date1 = strDate.split("-");
                LocalDate d1 = LocalDate.of(Integer.parseInt(date1[0]),Integer.parseInt(date1[1]),Integer.parseInt(date1[2]));
                String[] date2 = todo.getDate().split("-");
                LocalDate d2 = LocalDate.of(Integer.parseInt(date2[0]),Integer.parseInt(date2[1]),Integer.parseInt(date2[2]));
                if(d2.isBefore(d1))
                    todo.setStatus("overdue");
            }
            temp.add(todo);
        }
        TodoResource.todos = temp;
        return Response.status(Response.Status.OK).build();
    }
}
