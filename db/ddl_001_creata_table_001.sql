-- 企業テーブル
CREATE TABLE Companies
(
    CompanyID          INT AUTO_INCREMENT PRIMARY KEY,
    CorporateName      VARCHAR(255) NOT NULL,
    RepresentativeName VARCHAR(255) NOT NULL,
    PhoneNumber        VARCHAR(20),
    PostalCode         VARCHAR(10),
    Address            VARCHAR(255)
);

-- ユーザーテーブル (企業に紐づく)
CREATE TABLE Users
(
    UserID    INT AUTO_INCREMENT PRIMARY KEY,
    CompanyID INT,
    UserName  VARCHAR(255) NOT NULL,
    Email     VARCHAR(255) NOT NULL UNIQUE,
    Password  VARCHAR(255) NOT NULL,
    FOREIGN KEY (CompanyID) REFERENCES Companies (CompanyID)
);

-- 取引先テーブル (企業に紐づく)
CREATE TABLE Clients
(
    ClientID           INT AUTO_INCREMENT PRIMARY KEY,
    CompanyID          INT,
    CorporateName      VARCHAR(255) NOT NULL,
    RepresentativeName VARCHAR(255) NOT NULL,
    PhoneNumber        VARCHAR(20),
    PostalCode         VARCHAR(10),
    Address            VARCHAR(255),
    FOREIGN KEY (CompanyID) REFERENCES Companies (CompanyID)
);

-- 取引先銀行口座テーブル (取引先に紐づく)
CREATE TABLE ClientBankAccounts
(
    BankAccountID INT AUTO_INCREMENT PRIMARY KEY,
    ClientID      INT,
    BankName      VARCHAR(255) NOT NULL,
    BranchName    VARCHAR(255) NOT NULL,
    AccountNumber VARCHAR(20)  NOT NULL,
    AccountName   VARCHAR(255) NOT NULL,
    FOREIGN KEY (ClientID) REFERENCES Clients (ClientID)
);

-- 請求書データテーブル (企業・取引先に紐づく)
CREATE TABLE Invoices
(
    InvoiceID          INT AUTO_INCREMENT PRIMARY KEY,
    CompanyID          INT,
    ClientID           INT,
    IssueDate          DATE           NOT NULL,
    PaymentAmount      DECIMAL(10, 2) NOT NULL,
    Fee                DECIMAL(10, 2),
    FeeRate            DECIMAL(5, 2),
    ConsumptionTax     DECIMAL(10, 2),
    ConsumptionTaxRate DECIMAL(5, 2),
    InvoiceAmount      DECIMAL(10, 2) NOT NULL,
    PaymentDueDate     DATE,
    Status             ENUM('Pending', 'Processing', 'Paid', 'Error') NOT NULL,
    FOREIGN KEY (CompanyID) REFERENCES Companies (CompanyID),
    FOREIGN KEY (ClientID) REFERENCES Clients (ClientID)
);


-- 初期データ挿入

-- 企業テーブルの初期データ
INSERT INTO Companies (CorporateName, RepresentativeName, PhoneNumber, PostalCode, Address)
VALUES ('CompanyA', 'John Doe', '03-1234-5678', '100-0001', 'Tokyo, Chiyoda 1-1-1'),
       ('CompanyB', 'Jane Smith', '06-1234-5678', '530-0001', 'Osaka, Kita 1-1-1');

-- ユーザーテーブルの初期データ
INSERT INTO Users (CompanyID, UserName, Email, Password)
VALUES (1, 'Alice Johnson', 'alice@example.com', 'password1'),
       (1, 'Bob Brown', 'bob@example.com', 'password2'),
       (2, 'Charlie Davis', 'charlie@example.com', 'password3');

-- 取引先テーブルの初期データ
INSERT INTO Clients (CompanyID, CorporateName, RepresentativeName, PhoneNumber, PostalCode, Address)
VALUES (1, 'ClientA', 'Eve Black', '03-8765-4321', '100-0002', 'Tokyo, Chiyoda 2-2-2'),
       (2, 'ClientB', 'Frank White', '06-8765-4321', '530-0002', 'Osaka, Kita 2-2-2');

-- 取引先銀行口座テーブルの初期データ
INSERT INTO ClientBankAccounts (ClientID, BankName, BranchName, AccountNumber, AccountName)
VALUES (1, 'Mizuho Bank', 'Tokyo Central', '1234567890', 'ClientA Account'),
       (2, 'MUFG Bank', 'Osaka Central', '0987654321', 'ClientB Account');
