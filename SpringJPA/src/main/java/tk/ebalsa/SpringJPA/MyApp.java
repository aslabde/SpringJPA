package tk.ebalsa.SpringJPA;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@EnableJpaRepositories(basePackages = "tk.ebalsa.SpringJPA",
includeFilters = @ComponentScan.Filter(value = {BookRepository.class}, type = FilterType.ASSIGNABLE_TYPE))
@EnableTransactionManagement
public class MyApp {

    @Bean
    public DataSource dataSource() throws SQLException {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
      }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setGenerateDdl(true);

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setJpaVendorAdapter(vendorAdapter);
      factory.setPackagesToScan("tk.ebalsa.SpringJPA");
      factory.setDataSource(dataSource());
      factory.afterPropertiesSet();

      return factory.getObject();
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
      return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {

      JpaTransactionManager txManager = new JpaTransactionManager();
      txManager.setEntityManagerFactory(entityManagerFactory());
      return txManager;
    }
    
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
      return new HibernateExceptionTranslator();
    }

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(MyApp.class);
        BookRepository repository = context.getBean(BookRepository.class);

        // save a couple of books
        repository.save(new Book("title1", "auth1"));
        repository.save(new Book("Bible", "anonymus"));
        repository.save(new Book("Quijote", "Cervantes"));
        repository.save(new Book("Otelo", "Shakespeare"));
        repository.save(new Book("Chtulu", "Lovecraft"));

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

        context.close();
    }

}
