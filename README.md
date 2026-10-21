# Atmos-Weather-Application
# Introduction 
1 Importance of Weather Information Systems 
Weather information systems are critical for informed decision-making in sectors ranging from agriculture to daily logistics. They provide the necessary data to predict environmental conditions and mitigate risks associated with climate unpredictability. 
2 Limitations of Basic Weather Applications 
Most standard weather applications provide raw numerical data—temperature, humidity, and wind speed—but fail to provide actionable context. Users are left to interpret these figures manually, leading to uncertainty in daily planning. 
3 Need for Weather Analytics and Interpretation 
There is a pressing need for intelligent analytics that translate raw meteorological data into actionable advice. Transforming data into a "Comfort Score" or environmental recommendations adds significant value to the end user. 
4 Motivation Behind Developing ATMOS 
The motivation for ATMOS stems from the desire to create an application that bridges the gap between raw data and human intelligence, leveraging Java’s robust OOP capabilities to manage complex environmental datasets efficiently. 
5 Brief Overview of Application 
ATMOS is an intelligent desktop dashboard built with Java 17, Swing, and JFreeChart. It provides live weather analytics, comfort scores, and visual trends. 

 # Scope of Project
1 Real-World Problem Addressed 
ATMOS addresses the struggle of interpreting raw meteorological data for daily activities. By automating analysis, it helps users make smarter decisions about outdoor activities, commuting, and travel. 
2 Sustainable Development Goal (SDG 13 - Climate Action) 
By fostering weather awareness, ATMOS aligns with SDG 13. It encourages users to understand their environmental context, promoting climate action through informed daily habits and preparedness. 
3 Scope & Limitations 
Scope: Real-time data retrieval, comfort scoring, historical logging, and export functionality. 
Limitations: Requires an active internet connection; stores historical logs locally in .txt format rather than a centralized database. 
# Project Overview 

# System Workflow & Architecture 

User Input → WeatherAPI (Fetch) → WeatherData (Model) → WeatherAnalyzer (Process) → Dashboard/GraphPanel (UI View). 

5.2 GUI Modules / Forms 

Dashboard (Main View) 

Historical Analysis Panel 

City Comparison Panel 

Search History Panel 

Export Report View 

5.3 Features Implemented 

Live weather metrics 

Weather Comfort Score (0-100) 

Smart recommendations 

7-day trend analysis (JFreeChart) 

.txt file history/export 

5.4 Software Tools & Technology Stack 

Java 17+, Swing, IntelliJ IDEA, Open-Meteo API, JFreeChart 1.5.4. 

5.5 Java File Breakdown & Responsibilities 

Main.java: Entry point 

Dashboard.java: Main JFrame container 

WeatherData.java: Data encapsulation (POJO) 

WeatherAPI.java: REST connectivity 

WeatherAnalyzer.java: Logic processing 

GraphPanel.java: Chart rendering 

ComparePanel.java: Side-by-side analysis 

HistoryManager.java: ArrayList logging 

FileManager.java: File I/O operations 

Utils.java: Helper methods 

5.6 Object-Oriented Programming (OOP) Concepts 

Encapsulation: Private data fields in WeatherData. 

Inheritance: GUI components extending JPanel/JFrame. 

Polymorphism: Method overloading in logic modules. 

Interface usage for navigation. 

 

6. Results & Screenshots 

* Figure 1: ATMOS Main Dashboard 

* Figure 2: Smart Weather Analysis Output 

* Figure 3: Historical Temperature & Humidity Graphs 

* Figure 4: City Comparison Module 

* Figure 5: Exported Text Report File 

 

8. Code Snippets 

Encapsulation in WeatherData.java: 

public class WeatherData { 

    private double temperature; 

    public double getTemperature() { return temperature; } 

    public void setTemperature(double t) { this.temperature = t; } 

} 

REST API Connection in WeatherAPI.java: 

HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 

conn.setRequestMethod("GET"); 

Comfort Score Logic in WeatherAnalyzer.java: 

public int calculateComfort(double temp, double humidity) { 

    return score; // Logic: score = 100 - (abs(temp - 22) * 2); 

} 

ArrayList management in HistoryManager.java: 

private ArrayList<String> history = new ArrayList<>(); 

public void addEntry(String entry) { history.add(entry); } 

File I/O in FileManager.java: 

try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) { 

    writer.write(data); 

} 

 

Encapsulation in WeatherData.java: 

public class WeatherData { 

    private double temperature; 

    public double getTemperature() { return temperature; } 

    public void setTemperature(double t) { this.temperature = t; } 

} 

 

REST API Connection in WeatherAPI.java: 

HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 

conn.setRequestMethod("GET"); 

 

Comfort Score Logic in WeatherAnalyzer.java: 

public int calculateComfort(double temp, double humidity) { 

    return score; // Logic: score = 100 - (abs(temp - 22) * 2); 

} 

 

ArrayList management in HistoryManager.java: 

private ArrayList<String> history = new ArrayList<>(); 

public void addEntry(String entry) { history.add(entry); } 

 

8. Challenges Faced & Solutions 

REST API Integration: Resolved using HttpURLConnection and custom JSON parsing. 

Modern GUI Layouts: Resolved using CardLayout and custom borders. 

Asynchronous Operations: Resolved using SwingWorker. 

Chart Integration: Resolved using JFreeChart libraries and DefaultCategoryDataset. 

 

9. Learning Outcomes 

By completing this Micro Project, we learned to: 

Implement complex OOP structures in Java. 

Integrate external REST APIs into desktop applications. 

Manage file I/O operations and collections. 

Design responsive Swing GUIs. 

Apply defensive exception handling for network and file operations. 

 

10. Conclusion & Future Scope 

ATMOS successfully demonstrates the utility of Java in data analysis applications. Future scope includes SQL database integration for long-term analytics and push notification features. 

 

11. References 

Oracle Java Documentation 

Open-Meteo API Documentation 

JFreeChart Documentation 

Java Swing Tutorials 

 

 

 

 

 
