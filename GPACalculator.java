import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextArea;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GPACalculator {

  private static final String FILE_NAME = "courses.txt";
  private ArrayList<Course> courses;
  private JFrame frame;
  private JTextField courseNameField;
  private JTextField creditsField;
  private JComboBox<String> gradeBox;
  private JList<Course> coursesList;
  private JLabel gpaLabel;
  private JLabel creditsLayout;

  public GPACalculator() {
    courses = new ArrayList<Course>();
    createGUI();
    loadCoursesFromFile();
  }

  private void createGUI() {
    frame = new JFrame("GPA Calculator by Pasindu Prabhath");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BorderLayout());

    JPanel courseNamePanel = new JPanel();
    courseNamePanel.setLayout(new BorderLayout());

    JLabel courseNameLabel = new JLabel("Course Name:");
    courseNamePanel.add(courseNameLabel, BorderLayout.NORTH);

    courseNameField = new JTextField();
    courseNameField.setPreferredSize(new Dimension(200, 24));
    courseNamePanel.add(courseNameField, BorderLayout.CENTER);

    formPanel.add(courseNamePanel, BorderLayout.NORTH);

    JPanel creditsPanel = new JPanel();
    creditsPanel.setLayout(new BorderLayout());

    JLabel creditsLabel = new JLabel("Credits:");
    creditsPanel.add(creditsLabel, BorderLayout.NORTH);

    creditsField = new JTextField();
    creditsField.setPreferredSize(new Dimension(200, 24));
    creditsPanel.add(creditsField, BorderLayout.CENTER);

    formPanel.add(creditsPanel, BorderLayout.CENTER);

    JPanel gradePanel = new JPanel();
    gradePanel.setLayout(new BorderLayout());

    JLabel gradeLabel = new JLabel("Grade:");
    gradePanel.add(gradeLabel, BorderLayout.NORTH);

    String[] gradeOptions = {
      "A+",
      "A",
      "A-",
      "B+",
      "B",
      "B-",
      "C+",
      "C",
      "C-",
      "D+",
      "D",
      "D-",
      "F",
    };
    gradeBox = new JComboBox<String>(gradeOptions);
    gradeBox.setPreferredSize(new Dimension(200, 24));
    gradePanel.add(gradeBox, BorderLayout.CENTER);

    formPanel.add(gradePanel, BorderLayout.SOUTH);

    inputPanel.add(formPanel, BorderLayout.CENTER);

    JButton addCourseButton = new JButton("Add Course");
    addCourseButton.addActionListener(new AddCourseListener());
    inputPanel.add(addCourseButton, BorderLayout.WEST);

    JButton removeCourseButton = new JButton("Remove Course");
    removeCourseButton.addActionListener(new RemoveCourseListener());
    inputPanel.add(removeCourseButton, BorderLayout.EAST);

    mainPanel.add(inputPanel, BorderLayout.NORTH);

    coursesList = new JList<Course>();
    // coursesTextArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(coursesList);
    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
    verticalScrollBar.setPreferredSize(new Dimension(20, 0));
    verticalScrollBar.setVisible(true);
    scrollPane.setVerticalScrollBarPolicy(
      JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    );
    scrollPane.setPreferredSize(new Dimension(400, 300));
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    gpaLabel = new JLabel();
    creditsLayout = new JLabel();
    updateGPALabel();
    mainPanel.add(gpaLabel, BorderLayout.SOUTH);
    // mainPanel.add(creditsLayout, BorderLayout.NORTH);

    frame.add(mainPanel);
    frame.pack();
    frame.setVisible(true);
  }

  private void addCourse(String courseName, double credits, String grade) {
    Course course = new Course(courseName, credits, grade);
    courses.add(course);
    updateCoursesTextArea();
    saveCoursesToFile();
    updateGPALabel();
  }

  private void updateCoursesTextArea() {
    DefaultListModel<Course> model = new DefaultListModel<Course>();
    for (Course course : courses) {
      model.addElement(course);
    }
    coursesList.setModel(model);
    coursesList.setSelectedIndex(0);
  }

  private void updateGPALabel() {
    double totalCredits = 0.0;
    double totalGradePoints = 0.0;

    for (Course course : courses) {
      totalCredits += course.getCredits();
      totalGradePoints += course.getGradePoints();
    }

    double gpa = 0.0;
    if (totalCredits > 0.0) {
      gpa = totalGradePoints / totalCredits;
    }

    DecimalFormat df = new DecimalFormat("#.##");
    gpaLabel.setText("GPA: " + df.format(gpa));
    // creditsLayout.setText("Credits: Pasindu Prabhath");
  }

  private void loadCoursesFromFile() {
    File file = new File(FILE_NAME);
    if (file.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
          String[] tokens = line.split(",");
          String courseName = tokens[0];
          double credits = Double.parseDouble(tokens[1]);
          String grade = tokens[2];
          addCourse(courseName, credits, grade);
        }
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void saveCoursesToFile() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
      for (Course course : courses) {
        writer.write(
          course.getCourseName() +
          "," +
          course.getCredits() +
          "," +
          course.getGrade() +
          "\n"
        );
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class AddCourseListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      String courseName = courseNameField.getText();
      String creditsString = creditsField.getText();
      String grade = (String) gradeBox.getSelectedItem();

      if (courseName.isEmpty() || creditsString.isEmpty()) {
        return;
      }

      double credits = Double.parseDouble(creditsString);
      addCourse(courseName, credits, grade);
      courseNameField.setText("");
      creditsField.setText("");
      gradeBox.setSelectedIndex(0);
    }
  }

  private class RemoveCourseListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      int index = coursesList.getSelectedIndex();
      if (index >= 0) {
        courses.remove(index);
        updateCoursesTextArea();
        saveCoursesToFile();
        updateGPALabel();
      }
    }
  }

  private class Course {

    private String courseName;
    private double credits;
    private String grade;

    public Course(String courseName, double credits, String grade) {
      this.courseName = courseName;
      this.credits = credits;
      this.grade = grade;
    }

    public String getCourseName() {
      return courseName;
    }

    public double getCredits() {
      return credits;
    }

    public String getGrade() {
      return grade;
    }

    public double getGradePoints() {
      double gradePoints = 0.0;
      switch (grade) {
        case "A+":
          gradePoints = 4.0;
          break;
        case "A":
          gradePoints = 4.0;
          break;
        case "A-":
          gradePoints = 3.7;
          break;
        case "B+":
          gradePoints = 3.3;
          break;
        case "B":
          gradePoints = 3.0;
          break;
        case "B-":
          gradePoints = 2.7;
          break;
        case "C+":
          gradePoints = 2.3;
          break;
        case "C":
          gradePoints = 2.0;
          break;
        case "C-":
          gradePoints = 1.7;
          break;
        case "D+":
          gradePoints = 1.3;
          break;
        case "D":
          gradePoints = 1.0;
          break;
        case "D-":
          gradePoints = 0.7;
          break;
        case "F":
          gradePoints = 0.0;
          break;
      }
      return gradePoints * credits;
    }

    public String toString() {
      return courseName + " (" + credits + " credits, " + grade + ")";
    }
  }

  public static void main(String[] args) {
    new GPACalculator();
  }
}
