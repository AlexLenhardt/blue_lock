Write-Host "Building Lince Tech ERP"

# Add java 17 to the path
#$Java17Path = "C:\java-17-openjdk-17.0.4.1.1-1"
$Java17Path = "C:\Program Files\RedHat\java-17-openjdk-17.0.4.1.1-1"

# Call the build of the sever container
.\gradlew "-Dorg.gradle.java.home=$Java17Path" bootJar
