package stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class StudentExample {

  public static void main(String... __) {

    var alex = Student.builder().name("Alex").avgMark(4.5).build();
    var maria = Student.builder().name("Maria").avgMark(3.5).cost( 200 ).build();
    var john = Student.builder().name("John").age(12).course(4).avgMark(4.7).build();
    var bob = Student.builder().name("Bob").avgMark(4.8).cost( 30 ).build();
    var jack = Student.builder().name("Jack").age(23).avgMark(4.5).cost( 50 ).build();

    var students = List.of(alex, maria, john, bob, jack);

    /*
     * OLD (Java 1.0-1.7) style
     */
    var result = new ArrayList<Student>();
    // Напечатать имена топ-студентов 5го курса с оценкой больше 4, по убыванию
    for (Student student : students) {
      if ( student.getAvgMark() > 4
              && student.getCourse() == 5
              && student.getAge() == 22 )
        result.add( student );
    }

    result.sort((o1, o2) -> Double.compare(o2.getAvgMark(), o1.getAvgMark()));

    for (Student student : result)
      System.out.println(student.getName());

    /*
     * STREAM (Java1.8+) style
     */
    List<String> studentNames = students.stream()
        .filter(student -> student.getAvgMark() > 4)
        .filter(student -> student.getCourse() == 5)
        .filter(student -> student.getAge() == 22)
        .sorted(Comparator.comparingDouble(Student::getAvgMark).reversed())
        .map(Student::getName)
        .collect( Collectors.toList());

    Stream<Integer> stream1 = students.stream()
            .map( Student::getCost );


    Optional<Integer> avenue = students.stream()
            .map( Student::getCost )
            .reduce( (i1, i2) -> i1+i2 );

    avenue.ifPresent( System.out::println );

    int sum = students.stream()
            .mapToInt( Student::getCost ).sum();

    System.out.println(sum);


  }
}
