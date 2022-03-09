rootProject.name = "elevator-simulator"

include("application")



for (project in rootProject.children) {
    println("""print project name: ${project.name}""")
}