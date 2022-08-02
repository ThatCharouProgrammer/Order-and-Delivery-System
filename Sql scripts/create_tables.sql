DROP TABLE credit_card;
DROP TABLE cash;
DROP TABLE payment;
DROP TABLE order_details;
DROP TABLE order_tbl;
DROP TABLE delivery;
DROP TABLE vehicle;
DROP TABLE packer;
DROP TABLE driver;
DROP TABLE employee;
DROP TABLE product_review;
DROP TABLE product;
DROP TABLE category;
DROP TABLE customer;

 /* 1. */
CREATE TABLE customer(
	customerId VARCHAR(13) PRIMARY KEY NOT NULL,
	firstName VARCHAR(30) NOT NULL,
	surname VARCHAR(30), 
	phoneNumber VARCHAR(10) NOT NULL, 
	email VARCHAR(320),
	address TEXT NOT NULL,
	city VARCHAR(50) DEFAULT 'City not provided.', 
	province VARCHAR(50) DEFAULT 'Province not provided.',
	zipCode VARCHAR(4) NOT NULL
);

/* 3. */
CREATE TABLE category(
	categoryId VARCHAR(10) PRIMARY KEY NOT NULL,
	name VARCHAR(100) DEFAULT 'No name given.', 
	description TEXT DEFAULT ('No description given.')
);

/* 4. */
CREATE TABLE product(
	productId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	productName VARCHAR(150) DEFAULT 'No name given.',
	unitPrice DECIMAL(10,2) NOT NULL,
	color VARCHAR(30) NOT NULL,
	product_size VARCHAR(25) DEFAULT 'One size fits all.',
	quantityInStock INTEGER DEFAULT 0 CHECK(quantityInStock >= 0),
	categoryId VARCHAR(10) NOT NULL,
	CONSTRAINT size_check CHECK (product_size IN ('XS', 'S', 'M', 'L', 'XL', 'One size fits all.')),
	FOREIGN KEY (categoryId) REFERENCES category(categoryId)
);

/* 5. */
CREATE TABLE product_review(
	productReviewId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	customerId VARCHAR(13) NOT NULL, 
	productId INTEGER NOT NULL,
	title VARCHAR(30) NOT NULL,
	review TEXT NOT NULL, 
	rating INTEGER NOT NULL CHECK(rating>=0 OR rating<=10), 
	FOREIGN KEY (customerId) REFERENCES customer(customerId),
	FOREIGN KEY (productId) REFERENCES product(productId)	
);

/* 6. */
CREATE TABLE employee(
	employeeId VARCHAR(13) PRIMARY KEY NOT NULL,
	firstName VARCHAR(30) NOT NULL,
	surname VARCHAR(30),
	phoneNumber VARCHAR(10) NOT NULL,
	employeeType VARCHAR(1) NOT NULL,
	CONSTRAINT employeeType_check CHECK (employeeType IN ('P','D'))
);

/* 6.1. */
CREATE TABLE driver(
	employeeId VARCHAR(13) NOT NULL,
	licenseCode VARCHAR(3) NOT NULL,
	licenseExpiryDate DATE NOT NULL,
	salary DECIMAL(10,2) NOT NULL,
	CONSTRAINT licenseCode_check CHECK (licenseCode IN('A1','A','B','C1','C','EB','EC1','EC')),
	FOREIGN KEY (employeeId) REFERENCES employee(employeeId)
);

/* 6.2. */
CREATE TABLE packer(
	employeeId VARCHAR(13) NOT NULL,
	hourlyRate DECIMAL(10,2) NOT NULL,
	FOREIGN KEY (employeeId) REFERENCES employee(employeeId)
);

/* 7. */
CREATE TABLE vehicle(
	vin VARCHAR(17) PRIMARY KEY NOT NULL,
	make VARCHAR(30) DEFAULT 'Make not specified.',
	transmissionType VARCHAR(10) NOT NULL,
	fuelType VARCHAR(10) NOT NULL,
	vehicleType VARCHAR(30) NOT NULL,
	CONSTRAINT transmission_type_check CHECK (transmissionType IN ('AUTOMATIC', 'MANUAL')),
	CONSTRAINT fuel_type_check CHECK (fuelType IN ('PETROL', 'DIESEL')),
	CONSTRAINT vehicle_type_check CHECK (vehicleType IN ('TRUCK', 'BAKKIE'))
);

/* 8. */
CREATE TABLE delivery(
	deliveryId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	province VARCHAR(50) NOT NULL,
	dEmployeeId VARCHAR(13) NOT NULL,
	pEmployeeId VARCHAR(13) NOT NULL,
	vin VARCHAR(17) NOT NULL,
	deliveryStatus VARCHAR(15) NOT NULL,
	departureDate DATE NOT NULL,
	arrivalDate DATE,
	CONSTRAINT deliveryStatus_check CHECK (deliveryStatus IN ('IN TRANSIT', 'DELIVERED')),
	FOREIGN KEY (dEmployeeId) REFERENCES employee(employeeId),
	FOREIGN KEY (pEmployeeId) REFERENCES employee(employeeId),
	FOREIGN KEY (vin) REFERENCES vehicle(vin)
);

/* 9. */
CREATE TABLE order_tbl(
	orderId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	customerId VARCHAR(13) NOT NULL,
	deliveryId INTEGER NOT NULL,
	orderDate DATE NOT NULL,
	totalOrderPrice DECIMAL(10,2) DEFAULT 0,
	paid VARCHAR(3) DEFAULT "NO",
	CONSTRAINT paid_check CHECK (paid IN ('YES', 'NO')),
	FOREIGN KEY (customerId) REFERENCES customer(customerId),
	FOREIGN KEY (deliveryId) REFERENCES delivery(deliveryId)	
);

/* 10 */
CREATE TABLE order_details(
	orderDetailsId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	orderId INTEGER NOT NULL,
	productId INTEGER NOT NULL,
	quantityRequested INTEGER NOT NULL,
	totalPrice DECIMAL(10, 2) NOT NULL,
	employeeId VARCHAR(13) NOT NULL, 
	CONSTRAINT qty_request_check CHECK (quantityRequested > 0), 
	FOREIGN KEY (orderId) REFERENCES order_tbl(orderId),
	FOREIGN KEY (productId) REFERENCES product(productId),
	FOREIGN KEY (employeeId) REFERENCES employee(employeeId)
);

/* 2. */
CREATE TABLE payment(
	paymentId INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	customerId VARCHAR(13) NOT NULL,
	orderId INTEGER NOT NULL,
	paymentDate DATE DEFAULT (CURDATE()),
	paymentAmount DECIMAL(10,2),
	paymentType VARCHAR(2) NOT NULL, 
	CONSTRAINT paymentType_check CHECK (paymentType IN ('C','CR')),
	FOREIGN KEY (customerId) REFERENCES customer(customerId),
	FOREIGN KEY (orderId) REFERENCES order_tbl(orderId)
);


/* 2.1. */
CREATE TABLE cash(
	cashTransactionNumber VARCHAR(8) NOT NULL, 
	paymentId INTEGER NOT NULL,
	FOREIGN KEY (paymentId) REFERENCES payment(paymentId)
);

/* 2.2. */
CREATE TABLE credit_card(
	accountNumber VARCHAR(16) NOT NULL, 
	paymentId INTEGER NOT NULL,
	expiryDate DATE NOT NULL,
	securityCode VARCHAR(3) NOT NULL,
	FOREIGN KEY (paymentId) REFERENCES payment(paymentId)
);