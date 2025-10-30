classDiagram
    direction LR

    package "view" {
        class LoginFrame {
            +emailField: JTextField
            +passwordField: JPasswordField
            +studentRadio: JRadioButton
            +adminRadio: JRadioButton
            +loginButton: JButton
            +signUpButton: JButton
        }
        class SignUpFrame {
            +emailField: JTextField
            +passwordField: JPasswordField
            +confirmPasswordField: JPasswordField
            +signUpButton: JButton
            +backButton: JButton
        }
        class CanteenFrame {
            +welcomeLabel: JLabel
            +menuPanel: JPanel
            +billArea: JTextArea
            +placeOrderButton: JButton
        }
        class AdminFrame {
            +ordersArea: JTextArea
            +addItemButton: JButton
            +removeItemButton: JButton
            +logoutButton: JButton
        }
    }

    package "controller" {
        class LoginController {
            -loginFrame: LoginFrame
            -dbManager: DatabaseManager
            +LoginController(LoginFrame)
            +actionPerformed(ActionEvent)
            -openSignUpFrame()
            -handleLogin()
        }
        class SignUpController {
            -signUpFrame: SignUpFrame
            -dbManager: DatabaseManager
            +SignUpController(SignUpFrame)
            +actionPerformed(ActionEvent)
            -openLoginFrame()
            -handleSignUp()
        }
        class CanteenController {
            -canteenFrame: CanteenFrame
            -currentStudent: Student
            -dbManager: DatabaseManager
            -menuSpinners: Map~MenuItem,JSpinner~
            +CanteenController(CanteenFrame,Student)
            +actionPerformed(ActionEvent)
            +stateChanged(ChangeEvent)
            -loadMenuItems()
            -createOrderFromSelection()
            -updateBill()
            -handlePlaceOrder()
        }
        class AdminController {
            -adminFrame: AdminFrame
            -currentAdmin: Admin
            +AdminController(AdminFrame,Admin)
            +actionPerformed(ActionEvent)
            -loadAdminDetails()
        }
    }

    package "model" {
        class Student {
            -studentId: int
            -email: String
            +Student(int,String)
            +getStudentId(): int
            +getEmail(): String
        }
        class Admin {
            -adminId: int
            -email: String
            +Admin(int,String)
            +getAdminId(): int
            +getEmail(): String
        }
        class MenuItem {
            -itemId: int
            -name: String
            -price: double
            +MenuItem(int,String,double)
            +getItemId(): int
            +getName(): String
            +getPrice(): double
        }
        class Order {
            -items: List~OrderItem~
            +Order()
            +addItem(OrderItem)
            +getItems(): List~OrderItem~
            +getSubtotal(): double
        }
        class OrderItem {
            -item: MenuItem
            -quantity: int
            +OrderItem(MenuItem,int)
            +getItem(): MenuItem
            +getQuantity(): int
            +getTotalPrice(): double
        }
        class Bill {
            -order: Order
            -TAX_RATE: double = 0.05
            +Bill(Order)
            +getSubtotal(): double
            +getTax(): double
            +getTotal(): double
            +generateBillText(): String
        }
    }

    package "db" {
        class DatabaseManager {
            -DB_URL: String
            -USER: String
            -PASS: String
            +getConnection(): Connection
            +validateStudentLogin(String,String): Student
            +validateAdminLogin(String,String): Admin
            +createStudent(String,String): boolean
            +getMenuItems(): List~MenuItem~
            +saveOrder(Order,int): boolean
        }
    }

    ' Relationships
    LoginController "1" -- "1" LoginFrame : controls
    SignUpController "1" -- "1" SignUpFrame : controls
    CanteenController "1" -- "1" CanteenFrame : controls
    AdminController "1" -- "1" AdminFrame : controls

    LoginController "1" -- "1" DatabaseManager : uses
    SignUpController "1" -- "1" DatabaseManager : uses
    CanteenController "1" -- "1" DatabaseManager : uses

    CanteenController "1" -- "1" Student : manages
    AdminController "1" -- "1" Admin : manages

    DatabaseManager ..> Student : returns
    DatabaseManager ..> Admin : returns
    DatabaseManager ..> MenuItem : returns
    DatabaseManager ..> Order : accepts

    Order "1" o-- "*" OrderItem : contains
    OrderItem "1" -- "1" MenuItem : references
    Bill "1" -- "1" Order : calculates for
    CanteenController "1" -- "1" Order : creates
    CanteenController "1" -- "1" Bill : creates
