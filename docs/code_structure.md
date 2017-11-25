.
├── Alchem.iml ---------------------------------------> IML file create by IntelliJ IDEA
├── CODE_OF_CONDUCT.md -------------------------------> Code of conduct for Alchem Synergy as open source community
├── controlsfx-8.40.14.jar
├── database
│   └── AlchemDB.backup ------------------------------> Backup of latest schema of database (PostgreSQL) used in Alchem
├── KWOC.md  -----------------------------------------> Project Ideas for Kharagpur Winter of Code
├── LICENSE  -----------------------------------------> License (AGPL v3) information for Alchem
├── postgresql-9.4-1206-jdbc42.jar  ------------------> Jar file for PostgreSQL (API to provide database related classes and methods)
├── README.md  ---------------------------------------> Readme for Alchem
└── src
    ├── Main
    │   ├── ApplicationLauncher.java -----------------> Launches the Main Stage with login scene (`login_stage.fxml`)
    │   ├── Controllers
    │   │   ├── AddPurchaseController.java -----------> Controller class for `add_purchase.fxml`
    │   │   ├── AddSaleController.java ---------------> Controller class for `add_sale.fxml`
    │   │   ├── AlertScreenController.java -----------> Controller class for `alert_stage.fxml`
    │   │   ├── InventoryController.java -------------> Controller class for `inventory.fxml`
    │   │   ├── LoginController.java -----------------> Controller class for `login_stage.fxml`
    │   │   ├── MainDashboardSceneController.java ----> Controller class for `main_dashboard_scene.fxml`
    │   │   ├── MainFeaturesTabSceneController.java --> Controller class for `main_features_tab_scene.fxml`
    │   │   ├── MainHomeSceneController.java ---------> Controller class for `main_home_scene.fxml`
    │   │   ├── MainStageController.java -------------> Controller class for `main_stage.fxml`
    │   │   ├── NavigationDrawer
    │   │   │   ├── SettingsDrawerController.java ----> Controller class for `settings.fxml`
    │   │   │   └── UserDrawerController.java --------> Controller class for `user.fxml`
    │   │   ├── RegisterController.java --------------> Controller class for `register_stage.fxml`
    │   │   └── Retailers
    │   │       ├── ProfitLossController.java --------> Controller class for `profit_loss.fxml`
    │   │       ├── ViewPurchaseController.java ------> Controller class for `view_purchase.fxml`
    │   │       └── ViewSaleController.java ----------> Controller class for `view_sale.fxml`
    │   ├── ErrorAndInfo
    │   │   └── AlertBox.java ------------------------> Creates AlertBox whenever its constructor is called with required parameters.
    │   ├── Helpers
    │   │   ├── Add_purchase.java --------------------> Business logic for Adding a purchase pane
    │   │   ├── Billing_history.java -----------------> 
    │   │   ├── Billing.java ------------------------->
    │   │   ├── Medicine.java ------------------------>
    │   │   ├── Purchase_history.java ---------------->
    │   │   ├── Purchase.java ------------------------>
    │   │   ├── Retailers ---------------------------->
    │   │   │   ├── ProfitLoss.java ------------------>
    │   │   │   ├── Purchase.java -------------------->
    │   │   │   └── Sale.java ------------------------>
    │   │   └── UserInfo.java ------------------------> Fetches user info to use in source code wherever requried
    │   └── JdbcConnection
    │       └── JDBC.java ----------------------------> Establishes connection with database
    └── Resources
        ├── Assets
        │   ├── about.png
        │   ├── all_features.png
        │   ├── dashboard.png
        │   ├── home.png
        │   ├── inventory.png
        │   ├── purchase.png
        │   ├── sale.png
        │   ├── setting.png
        │   ├── user_dummy.png
        │   ├── user.png
        │   ├── view_purchase.png
        │   └── view_sale.png
        ├── Layouts
        │   ├── add_sale.fxml ------------------------> Layout for "Add Sale" tab
        │   ├── alert_stage.fxml ---------------------> Layout for "Alert" stage
        │   ├── inventory.fxml -----------------------> Layout for "Inventory" tab
        │   ├── login_stage.fxml ---------------------> Layout for "Login" stage
        │   ├── main_dashboard_scene.fxml ------------> Layout for "Dashboard" pane on "Main" stage.
        │   ├── main_features_tab_scene.fxml ---------> Layout for "Main Features" pane (tabbed layout of all features) on "Main" stage.
        │   ├── main_home_scene.fxml -----------------> Layout for "Home" pane on "Main" stage.
        │   ├── main_stage.fxml ----------------------> Layout for "Main" stage.
        │   ├── NavigationDrawer
        │   │   ├── settings.fxml --------------------> Layout for "Settings" navigation drawer.
        │   │   └── user.fxml ------------------------> Layout for "User Info" navigation drawer.
        │   ├── register_stage.fxml ------------------> Layout for "Register" stage.
        │   └── Retailers
        │       ├── add_purchase.fxml ----------------> Layout for "Add Purchase" tab.
        │       ├── profit_loss.fxml -----------------> Layout for "Profit/Loss analysis" tab.
        │       ├── view_purchase.fxml ---------------> Layout for "View Purchase" tab.
        │       └── view_sale.fxml -------------------> Layout for "View Sale" tab.
        └── Styling
            ├── add_purchase.css ---------------------> Stylesheet for `add_purchase.fxml`
            ├── add_sale.css -------------------------> Stylesheet for `add_sale.fxml`
            ├── dashboard.css ------------------------> Stylesheet for `main_dashboard_scene.fxml`
            ├── inventory.css ------------------------> Stylesheet for `inventory.fxml`
            ├── menu.css -----------------------------> Stylesheet for Menu bar on login stage
            ├── Retailers 
            │   ├── profit_loss.css ------------------> Stylesheet for `profit_loss.fxml`
            │   ├── view_purchase.css ----------------> Stylesheet for `view_purchase.fxml`
            │   └── view_sale.css --------------------> Stylesheet for `view_sale.fxml`
            └── tab_pane.css -------------------------> Stylesheet for "TabPane" element.

