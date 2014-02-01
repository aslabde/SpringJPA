package tk.ebalsa.SpringJPA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Book.findByEdit", query = "SELECT b FROM Book b WHERE LOWER(b.editor) = LOWER(?1)")
public class Book {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String title;
	private String author;
	private String editor;
	private double prize;
	
	protected Book(){};
	
	public Book(String title, String author, String editor, double prize){
		this.title=title;
		this.author=author;
		this.editor=editor;
		this.prize=prize;
	}
	
	@Override
	public String toString(){
		return String.format(
                "Book[id=%d, title='%s', author='%s']",
                id, title, author);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public double getPrize() {
		return prize;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}
	
}
