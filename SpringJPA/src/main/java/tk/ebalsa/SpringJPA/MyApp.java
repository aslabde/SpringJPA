package tk.ebalsa.SpringJPA;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories
public class MyApp {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(dataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPackagesToScan("tk.ebalsa.SpringJPA");
        return lef;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(MyApp.class);
        BookRepository repository = context.getBean(BookRepository.class);

        // save a couple of books
        repository.save(new Book("title1", "auth1", "edit", 50.00));
        repository.save(new Book("Bible", "anonymus","edit", 80.00));
        repository.save(new Book("Quijote", "Cervantes","edit", 79.99));
        repository.save(new Book("Otelo", "Shakespeare","edit", 42.00));
        repository.save(new Book("Chtulu", "Lovecraft","aaa", 38.38));

        // fetch all books
        Iterable<Book> books = repository.findAll();
        System.out.println("Book found with findAll():");
        System.out.println("-------------------------------");
        for (Book b : books) {
            System.out.println(b);
        }
        System.out.println();

        // fetch an individual book by ID
        Book book = repository.findOne(1L);
        System.out.println("Book found with findOne(1L):");
        System.out.println("--------------------------------");
        System.out.println(book);
        System.out.println();

        // fetch books by author
        List<Book> Books = repository.findByAuthor("Cervantes");
        System.out.println("Book found with Author('Cervantes'):");
        System.out.println("--------------------------------------------");
        for (Book b : Books) {
            System.out.println(b);
        }
        
        // fetch books by editor @namedQuery
        List<Book> BooksEdit = repository.findByEdit("edit");
        System.out.println("Book found with Editor('edit'):");
        System.out.println("--------------------------------------------");
        for (Book b : BooksEdit) {
            System.out.println(b);
        }

        context.close();
    }

}
