-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 29, 2018 at 11:17 PM
-- Server version: 10.2.10-MariaDB
-- PHP Version: 7.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `demo`
--

-- --------------------------------------------------------

--
-- Table structure for table `bus`
--

CREATE TABLE `bus` (
  `id` int(15) NOT NULL,
  `Bus_ID` varchar(50) NOT NULL,
  `FirstName` varchar(20) NOT NULL,
  `LastName` varchar(20) NOT NULL,
  `Card_Reader_ID1` varchar(50) NOT NULL,
  `Card_Reader_ID2` varchar(50) NOT NULL,
  `Controller_ID` varchar(50) NOT NULL,
  `AdPanelID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bus`
--

INSERT INTO `bus` (`id`, `Bus_ID`, `FirstName`, `LastName`, `Card_Reader_ID1`, `Card_Reader_ID2`, `Controller_ID`, `AdPanelID`) VALUES
(1, 'S12345678', '', '', '1A-S12345678-D1', '1A-S12345678-D2', '9999999', '1A-S12345678-AD'),
(2, 'S98765432', 'fec', 'das', '2B-98765432-D1', '2B-98765432-D2', '2B-98765432-C', '2B-98765432-AD');

-- --------------------------------------------------------

--
-- Table structure for table `busroute`
--

CREATE TABLE `busroute` (
  `id` int(15) NOT NULL,
  `BusRoute` varchar(20) NOT NULL,
  `Bus_ID` varchar(20) NOT NULL,
  `Zone` varchar(50) NOT NULL,
  `CutLat` varchar(50) DEFAULT NULL,
  `CurLong` varchar(50) DEFAULT NULL,
  `CurGPS_Location_1` varchar(50) DEFAULT NULL,
  `CurGPS_Location_2` varchar(50) DEFAULT NULL,
  `CurETA` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `busroute`
--

INSERT INTO `busroute` (`id`, `BusRoute`, `Bus_ID`, `Zone`, `CutLat`, `CurLong`, `CurGPS_Location_1`, `CurGPS_Location_2`, `CurETA`) VALUES
(3, '1A', 'S12345678', 'Gek Poh', '', '', '', '', ''),
(4, '2B', 'S98765432', 'Pioneer', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `busstops`
--

CREATE TABLE `busstops` (
  `id` int(15) NOT NULL,
  `BusRoute` varchar(255) NOT NULL,
  `Stops` varchar(50) NOT NULL,
  `Lat` varchar(50) NOT NULL,
  `Longtitude` varchar(50) NOT NULL,
  `GPS_Location_1` varchar(50) NOT NULL,
  `GPS_Location_2` varchar(50) NOT NULL,
  `Poly_Cord_lat1` varchar(50) NOT NULL,
  `Poly_Cord_long1` varchar(50) NOT NULL,
  `Poly_Cord_lat2` varchar(50) NOT NULL,
  `Poly_Cord_long2` varchar(50) NOT NULL,
  `Poly_Cord_lat3` varchar(50) NOT NULL,
  `Poly_Cord_long3` varchar(50) NOT NULL,
  `Poly_Cord_lat4` varchar(50) NOT NULL,
  `Poly_Cord_long4` varchar(50) NOT NULL,
  `Poly_Cord_lat5` varchar(50) NOT NULL,
  `Poly_Cord_long5` varchar(50) NOT NULL,
  `Poly_Cord_lat6` varchar(50) NOT NULL,
  `Poly_Cord_long6` varchar(50) NOT NULL,
  `Poly_Cord_lat7` varchar(50) NOT NULL,
  `Poly_Cord_long7` varchar(50) NOT NULL,
  `Poly_Cord_lat8` varchar(50) NOT NULL,
  `Poly_Cord_long8` varchar(50) NOT NULL,
  `Poly_Cord_lat9` varchar(50) NOT NULL,
  `Poly_Cord_long9` varchar(50) NOT NULL,
  `Poly_Cord_lat10` varchar(50) NOT NULL,
  `Poly_Cord_long10` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `busstops`
--

INSERT INTO `busstops` (`id`, `BusRoute`, `Stops`, `Lat`, `Longtitude`, `GPS_Location_1`, `GPS_Location_2`, `Poly_Cord_lat1`, `Poly_Cord_long1`, `Poly_Cord_lat2`, `Poly_Cord_long2`, `Poly_Cord_lat3`, `Poly_Cord_long3`, `Poly_Cord_lat4`, `Poly_Cord_long4`, `Poly_Cord_lat5`, `Poly_Cord_long5`, `Poly_Cord_lat6`, `Poly_Cord_long6`, `Poly_Cord_lat7`, `Poly_Cord_long7`, `Poly_Cord_lat8`, `Poly_Cord_long8`, `Poly_Cord_lat9`, `Poly_Cord_long9`, `Poly_Cord_lat10`, `Poly_Cord_long10`) VALUES
(2, '1A', 'XII', '1.344232', '103.696132', '1° 20\' 39.2352\'\' N', '103° 41\' 46.0752\'\' E', '1', '22222', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(3, '1A', 'XIII', '1.34287', '103.695558', '1° 20\' 34.332\'\' N', '103° 41\' 44.0088\'\' E', '1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(4, '1A', 'XIV', '1.342527', '103.697274', '1° 20\' 33.0972\'\' N', '103° 41\' 50.1864\'\' E', '1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(5, '1A', 'XV', '1.345374', '103.699409', '1° 20\' 43.3464\'\' N', '103° 41\' 57.8724\'\' E', '1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(6, '1', '2', '1', '1', '1', '1', '1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(7, 'vwv', 'wvw', 'vwdvw', 'vwdv', 'vwvwdv', 'vwdvwv', 'vwdvwdv', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''),
(8, '', '', 'peroj', '', 'gwrmw', 'gwrklb;w', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `card`
--

CREATE TABLE `card` (
  `id` int(11) NOT NULL,
  `UserID` varchar(50) DEFAULT NULL,
  `FirstName` varchar(20) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `Isblock` int(20) NOT NULL,
  `ValidFrom` varchar(20) NOT NULL,
  `ValidTo` varchar(20) NOT NULL,
  `Balance` int(20) NOT NULL,
  `CardNumber` int(20) NOT NULL,
  `FareType` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `card`
--

INSERT INTO `card` (`id`, `UserID`, `FirstName`, `LastName`, `Isblock`, `ValidFrom`, `ValidTo`, `Balance`, `CardNumber`, `FareType`) VALUES
(1, 'cr00000001', 'john', '', 0, '12', 'Active', 666, 1235, 0),
(2, 'cr00000031', 'jane', 'mol', 0, '2-Dec-18', '18-May-99', 230, 6523, 1),
(3, 'cr00000003', 'das', 'but', 0, '16-Dec-18', '16-Dec-99', 254, 1549, 2),
(4, 'cr00000004', 'ken', 'keb', 0, '15-Jun-18', '15-jun-99', 4665, 2359, 3),
(5, NULL, 'Dx', 'ST', 0, '03-SEP-18', '31-AUG-18', 0, 633567, 2),
(6, NULL, 'Dx', 'ST', 0, '03-SEP-18', '31-AUG-18', 0, 63289567, 2);

-- --------------------------------------------------------

--
-- Table structure for table `driver`
--

CREATE TABLE `driver` (
  `id` int(11) NOT NULL,
  `FirstName` varchar(20) NOT NULL,
  `LastName` varchar(20) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Mobile` varchar(20) NOT NULL,
  `City` varchar(20) NOT NULL,
  `State` varchar(20) NOT NULL,
  `Pincode` varchar(20) NOT NULL,
  `Age` int(50) NOT NULL,
  `Bus_ID` varchar(20) NOT NULL,
  `isEVP` varchar(20) NOT NULL,
  `isOTP` varchar(20) NOT NULL,
  `Password` varchar(50) NOT NULL,
  `Created_Date` varchar(20) NOT NULL,
  `Updated_Date` varchar(20) NOT NULL,
  `FirstNameAC` varchar(20) NOT NULL,
  `LastNameAC` varchar(50) NOT NULL,
  `Account_Number` varchar(20) NOT NULL,
  `IFSC` varchar(20) NOT NULL,
  `AddressLine1` varchar(20) NOT NULL,
  `AddressLine2` varchar(50) NOT NULL,
  `AddressLine3` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `driver`
--

INSERT INTO `driver` (`id`, `FirstName`, `LastName`, `Email`, `Mobile`, `City`, `State`, `Pincode`, `Age`, `Bus_ID`, `isEVP`, `isOTP`, `Password`, `Created_Date`, `Updated_Date`, `FirstNameAC`, `LastNameAC`, `Account_Number`, `IFSC`, `AddressLine1`, `AddressLine2`, `AddressLine3`) VALUES
(1, 'Rapunzel', 'Pizzerbert', 'eugene@gmail.com', '1915352641', 'shen yang', 'excellent', '5766967', 90, 'S12345678', '5409323', '3186224', '5679308', '2018-07-06', 'deactivated', 'EugenePizzerbert', '', '19950901', '19900901', 'ochob science', '', ''),
(2, 'Manguzi', 'den', 'eugene@gmail.com', '12321321', 'dandong', 'excellent', '3425435', 34, 'S98765432', '543534', '346456', '56735', '2018-07-06', '2018-07-22', 'BN', '', '477568', '256265', 'ochob science', '', ''),
(3, 'rapo', 'gal ', 'eugene@gmail.com', '324234325', 'benxi', 'excellent', '266547', 54, 'S11112222', '54754', '6575', '547636', '2018-07-06', '2018-07-22', 'HG', '', '546387', '35746', 'ochob science', '', ''),
(4, 'deny', 'kop', 'eugene@gmail.com', '5433625', 'chaochao', 'excellent', '345634', 64, 'S11221122', '54768', '46457', '2245', '2018-07-06', '2018-07-22', 'YR', '', '26878', '56346', 'ochob science', '', ''),
(5, 'john', 'ten', 'eugene@gmail.com', '64654743', 'hongkong', 'excellent', '3453', 23, 'S22221111', '8373', '347', '264', '2018-07-06', '2018-07-22', 'EDF', '', '8946', '73567', 'ochob science', '', ''),
(6, 'sara', 'kal', 'eugene@gmail.com', '2346542', 'Taiwan', 'bad', '976867', 62, 'S11111111', '6838', '4356', '26546', '2018-07-06', '2018-07-22', 'gH', '', '579403', '24726', 'ochob science', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `dtransaction`
--

CREATE TABLE `dtransaction` (
  `id` int(11) NOT NULL,
  `busId` varchar(20) NOT NULL,
  `AFCAmount` varchar(20) NOT NULL,
  `MFCAmount` varchar(20) NOT NULL,
  `MFCCAmount` varchar(50) NOT NULL,
  `MuflarCommission` varchar(50) NOT NULL,
  `TotalAmount` varchar(50) NOT NULL,
  `RideAmount` varchar(50) NOT NULL,
  `TopUpAmount` varchar(50) NOT NULL,
  `DateStart` varchar(50) NOT NULL,
  `DateEnd` varchar(50) NOT NULL,
  `Week` varchar(50) NOT NULL,
  `year` varchar(22) NOT NULL,
  `transactionStatus` varchar(22) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `dtransaction`
--

INSERT INTO `dtransaction` (`id`, `busId`, `AFCAmount`, `MFCAmount`, `MFCCAmount`, `MuflarCommission`, `TotalAmount`, `RideAmount`, `TopUpAmount`, `DateStart`, `DateEnd`, `Week`, `year`, `transactionStatus`) VALUES
(1, 'S98765432', '22.0875', '0', '7.3625', '2.55', '-100.55', '29.45', '130', '2018-07-15', '2018-07-21', '28', '2018', ''),
(2, 'S12345678', '36.9', '0', '2.3625', '5.7375', '-160.7375', '39.2625', '200', '2018-12-02', '2018-12-08', '48', '2018', ''),
(3, 'S12345678', '8.725', '0', '0', '1.275', '-111.275', '8.725', '120', '2018-12-16', '2018-12-22', '50', '2018', ''),
(4, 'S98765432', '15.725', '0', '0', '1.275', '-29.275', '15.725', '45', '2018-06-17', '2018-06-23', '24', '2018', '');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `UserID` int(50) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`UserID`, `Name`, `Password`) VALUES
(1, 'Debanjan', 'Test123'),
(2, 'a', 'a');

-- --------------------------------------------------------

--
-- Table structure for table `reportblock`
--

CREATE TABLE `reportblock` (
  `id` int(11) NOT NULL,
  `UserID` varchar(50) NOT NULL,
  `ReportID` varchar(20) NOT NULL,
  `Report_Type` varchar(20) NOT NULL,
  `CardID` int(20) NOT NULL,
  `Credit_Amount` float NOT NULL,
  `Credit_Type` varchar(20) NOT NULL,
  `Report_Date` varchar(20) NOT NULL,
  `FirstNameAC` varchar(20) NOT NULL,
  `LastNameAC` varchar(50) NOT NULL,
  `Account_Number` int(20) NOT NULL,
  `IFSC` int(20) NOT NULL,
  `AddressLine1` varchar(20) NOT NULL,
  `AddressLine2` varchar(50) NOT NULL,
  `AddressLine3` varchar(50) NOT NULL,
  `Pincode` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reportblock`
--

INSERT INTO `reportblock` (`id`, `UserID`, `ReportID`, `Report_Type`, `CardID`, `Credit_Amount`, `Credit_Type`, `Report_Date`, `FirstNameAC`, `LastNameAC`, `Account_Number`, `IFSC`, `AddressLine1`, `AddressLine2`, `AddressLine3`, `Pincode`) VALUES
(3, 'cr00000002', 'E3', 'IPhone', 6523, 3, 'Cash at home', '12-Jun-17', 'Jane', 'mol', 26856, 432, '235 vds kol 06', 'asdf', 'asqw', 2345),
(6, '123', '123', '213', 123, 213, '213', '123', '21321', '312', 3, 213123, '123', '123', '12312312', 3),
(21, '123', '123', '213', 231, 213, '', '', '', '', 0, 0, '', '', '', 0),
(22, '1', '1', '1', 1, 1, '11', '1', '1', '1', 1, 1, '1', '1', '1', 1);

-- --------------------------------------------------------

--
-- Table structure for table `reportchangedevice`
--

CREATE TABLE `reportchangedevice` (
  `id` int(11) NOT NULL,
  `UserID` varchar(20) NOT NULL,
  `ReportID` varchar(20) NOT NULL,
  `Report_Type` varchar(20) NOT NULL,
  `Bus_ID` varchar(20) NOT NULL,
  `BusRoute` varchar(50) NOT NULL,
  `Old_CardReaderID1` varchar(20) NOT NULL,
  `Old_CardReaderID2` varchar(20) NOT NULL,
  `Old_ControllerID` varchar(20) NOT NULL,
  `Old_AdPanelID` varchar(20) NOT NULL,
  `New_CardReaderID1` varchar(20) NOT NULL,
  `New_CardReaderID2` varchar(20) NOT NULL,
  `New_ControllerID` varchar(20) NOT NULL,
  `New_AdPanelID` varchar(20) NOT NULL,
  `Report_Date` varchar(20) NOT NULL,
  `Status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reportchangedevice`
--

INSERT INTO `reportchangedevice` (`id`, `UserID`, `ReportID`, `Report_Type`, `Bus_ID`, `BusRoute`, `Old_CardReaderID1`, `Old_CardReaderID2`, `Old_ControllerID`, `Old_AdPanelID`, `New_CardReaderID1`, `New_CardReaderID2`, `New_ControllerID`, `New_AdPanelID`, `Report_Date`, `Status`) VALUES
(1, '2B-S98765432', 'C1', 'Android App', 'S98765432', '2B', '', '', '', '', '', '', '', '', '12/2/2019', 'WIP'),
(2, 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b');

-- --------------------------------------------------------

--
-- Table structure for table `reportchangeroute`
--

CREATE TABLE `reportchangeroute` (
  `id` int(11) NOT NULL,
  `UserID` varchar(20) NOT NULL,
  `ReportID` varchar(20) NOT NULL,
  `Report_Type` varchar(20) NOT NULL,
  `Bus_ID` varchar(20) NOT NULL,
  `New_Route` varchar(20) NOT NULL,
  `TransferBusID` varchar(20) NOT NULL,
  `Report_Date` varchar(20) NOT NULL,
  `Status` varchar(20) NOT NULL,
  `New_UserID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reportchangeroute`
--

INSERT INTO `reportchangeroute` (`id`, `UserID`, `ReportID`, `Report_Type`, `Bus_ID`, `New_Route`, `TransferBusID`, `Report_Date`, `Status`, `New_UserID`) VALUES
(2, 'f', 'f', 'f', 'f', 'f', 'f', 'b', 'b', 'b');

-- --------------------------------------------------------

--
-- Table structure for table `reportservice`
--

CREATE TABLE `reportservice` (
  `id` int(11) NOT NULL,
  `UserID` varchar(20) NOT NULL,
  `ReportID` varchar(20) NOT NULL,
  `Report_Type` varchar(20) NOT NULL,
  `Issue` varchar(20) NOT NULL,
  `DeviceID` varchar(20) NOT NULL,
  `Device` varchar(20) NOT NULL,
  `Report_Date` varchar(20) NOT NULL,
  `Status` varchar(20) NOT NULL,
  `ServicedBY` varchar(20) NOT NULL,
  `BusID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reportservice`
--

INSERT INTO `reportservice` (`id`, `UserID`, `ReportID`, `Report_Type`, `Issue`, `DeviceID`, `Device`, `Report_Date`, `Status`, `ServicedBY`, `BusID`) VALUES
(4, 'r', '', '', '', '', '', '', '', 'r', ''),
(6, '12', '12', '12', '21', '122', '21', '2121', '21', '21', ''),
(7, '2', '2', '2', '2', '2', '', '', '2', '2', '');

-- --------------------------------------------------------

--
-- Table structure for table `ridehistory`
--

CREATE TABLE `ridehistory` (
  `id` int(11) NOT NULL,
  `UserID` varchar(50) NOT NULL,
  `CardID` int(20) NOT NULL,
  `BusRoute` varchar(50) NOT NULL,
  `BusID` varchar(20) NOT NULL,
  `Entry` varchar(20) NOT NULL,
  `Exit` varchar(20) NOT NULL,
  `FareCharged` float NOT NULL,
  `Travel_Date` date NOT NULL,
  `week` int(50) NOT NULL,
  `Entry_Time` varchar(20) NOT NULL,
  `Exit_Time` varchar(20) NOT NULL,
  `Entry_CardReaderID` varchar(20) NOT NULL,
  `Exit_CardReaderID` varchar(20) NOT NULL,
  `Fare_Type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ridehistory`
--

INSERT INTO `ridehistory` (`id`, `UserID`, `CardID`, `BusRoute`, `BusID`, `Entry`, `Exit`, `FareCharged`, `Travel_Date`, `week`, `Entry_Time`, `Exit_Time`, `Entry_CardReaderID`, `Exit_CardReaderID`, `Fare_Type`) VALUES
(1, 'cr00000001', 1235, '1A', 'S12345678', 'XI', 'XV', 6, '2018-01-01', 1, '9:30 AM', '10:15 AM', '1A-S12345678-D1', '1A-S12345678-D2', 'AFC'),
(3, 'cr00000002', 1549, '1A', 'S12345678', 'XII', 'XV', 5, '2018-02-02', 5, '9:17 AM', '9:47 AM', '1A-S12345678-D1', '', 'AFC'),
(4, 'cr00000003', 2359, '2B', 'S98765432', 'B', 'E', 9, '2018-01-04', 1, '8:45 PM', '9:15 PM', '2B-S98765432-D1', '2B-S98765432-D2', 'AFC'),
(5, 'cr00000004', 1235, '1A', 'S12345678', 'XIV', 'XV', 3, '2018-01-02', 1, '12:30 PM', '1:30 PM', '1A-S12345678-D1', '1A-S12345678-D2', 'AFC'),
(6, 'cr00000001', 6523, '2B', 'S98765432', 'A', 'D', 8, '2018-01-31', 4, '8:25 AM', '8:55 AM', '2B-S98765432-D2', '2B-S98765432-D2', 'AFC'),
(7, 'cr00000002', 1549, '1A', 'S12345678', 'XII', 'XV', 5, '2018-11-30', 48, '7:40 PM', '8:20 PM', '1A-S12345678-D1', '1A-S12345678-D2', 'AFC'),
(8, 'cr00000003', 2359, '2B', 'S98765432', 'B', 'E', 8, '2018-12-01', 48, '8:18 AM', '8:58 AM', '2B-S98765432-D1', '2B-S98765432-D2', 'AFC'),
(9, 'cr00000004', 1235, '1A', 'S12345678', 'XIV', 'XV', 3, '2018-10-01', 39, '7:32 AM', '8:12 AM', '1A-S12345678-D1', '1A-S12345678-D2', 'AFC'),
(10, 'm00000002 ', 6523, '2B', 'S98765432', 'B', 'E', 8, '2018-09-01', 35, '4:20 PM', '4:50 PM', '2B-S98765432-D1', '2B-S98765432-D2', 'AFC'),
(11, 'cr00000001', 1235, '2B', 'S98765432', 'A', 'D', 8, '2018-09-02', 35, '12:30 PM', '1:30 PM', '2B-S98765432-D1', '2B-S98765432-D1', 'MFC'),
(12, 'cr00000002', 1235, '1A', 'S12345678', 'XIV', 'XV', 3, '2018-09-03', 35, '7:32 AM', '8:12 AM', '1A-S12345678-D1', '1A-S12345678-D2', 'MFC-C'),
(13, 'cr00000002', 6523, '2B', 'S98765432', 'B', 'E', 8, '2018-09-04', 35, '4:20 PM', '4:50 PM', '2B-S98765432-D1', '2B-S98765432-D2', 'MFC-C'),
(14, 'cr00000002', 3333, '1A', 'S12345678', 'XI', 'XV', 6, '2018-01-01', 51, '9:30 AM', '10:15 AM', '1A-S12345678-D1', '1A-S12345678-D2', 'AFC');

-- --------------------------------------------------------

--
-- Table structure for table `routefare`
--

CREATE TABLE `routefare` (
  `id` int(11) NOT NULL,
  `BusRoute` varchar(50) NOT NULL,
  `From_route` varchar(50) NOT NULL,
  `To_route` varchar(50) NOT NULL,
  `Fare` varchar(50) NOT NULL,
  `FareType` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `routefare`
--

INSERT INTO `routefare` (`id`, `BusRoute`, `From_route`, `To_route`, `Fare`, `FareType`) VALUES
(1, '1A', 'XI', 'XII', '3', '1'),
(2, '1A', 'XI', 'XIII', '4', '1'),
(3, '1A', 'XI', 'XIV', '5', '1'),
(4, '1A', 'XI', 'XV', '6', '1'),
(5, '1A', 'XII', 'XIII', '3', '1'),
(6, '1A', 'XII', 'XIV', '4', '0'),
(7, '1A', 'XII', 'XV', '5', '0'),
(8, '1A', 'XIII', 'XIV', '3', '0'),
(9, '1A', 'XII', 'XV', '4', '1'),
(10, '1A', 'XIV', 'XV', '3', '0'),
(11, '1A', 'XII', 'XI', '3', '0'),
(12, '1A', 'XIII', 'XI', '4', '2'),
(13, '1A', 'XIV', 'XI', '5', '0'),
(14, '1A', 'XV', 'XI', '6', '0'),
(15, '1A', 'XIII', 'XII', '3', '2'),
(16, '1A', 'XIV', 'XII', '4', '3'),
(17, '1A', 'XV', 'XII', '5', '0'),
(18, '1A', 'XIV', 'XIII', '3', '0'),
(19, '1A', 'XV', 'XIII', '4', '1'),
(20, '1A', 'XV', 'XIV', '3', '0'),
(26, '2B', 'B', 'D', '4', '1'),
(27, '2B', 'B', 'E', '5', '0'),
(28, '2B', 'C', 'D', '3', '3'),
(29, '2B', 'C', 'E', '4', '0'),
(30, '2B', 'D', 'E', '3', '2'),
(31, '2B', 'B', 'A', '3', '0'),
(32, '2B', 'C', 'A', '4', '0'),
(33, '2B', 'D', 'A', '5', '0'),
(34, '2B', 'E', 'A', '6', '0'),
(35, '2B', 'C', 'B', '3', '0'),
(36, '2B', 'B', 'D', '4', '0'),
(37, '2B', 'B', 'E', '5', '0'),
(38, '2B', 'C', 'D', '3', '0'),
(39, '2B', 'C', 'E', '4', '0'),
(40, '2B', 'D', 'E', '3', '0'),
(41, '2B', 'B', 'A', '3', '0'),
(42, '2B', 'C', 'A', '4', '0'),
(43, '2B', 'D', 'A', '5', '0'),
(44, '2B', 'E', 'A', '6', '0'),
(45, '4B', 'H', 'K', '10', '0'),
(46, '2B', 'D', 'B', '4', '0'),
(47, '2B', 'E', 'B', '5', '0'),
(48, '2B', 'D', 'C', '3', '0'),
(49, '2B', 'E', 'C', '4', '0'),
(50, '2B', 'E', 'D', '3', '0'),
(51, '4B', 'H', 'e', '10', '0'),
(52, '3C', 'T', 'M', '5', '0'),
(53, 'K', 'K', 'K', '100', '0'),
(54, '8K', 'XH', 'XII', '5', '0'),
(55, 'FD', 'XI', 'XII', '3', '0'),
(56, 'FD', 'XI', 'XII', '3', '0'),
(57, 'FD', 'XI', 'XII', '3', '0'),
(58, 'FD', 'XI', 'XII', '3', '0'),
(59, 'FD', 'XI', 'XII', '3', '0'),
(60, 'FD', 'XI', 'XII', '3', '0'),
(61, 'wdvw', ' wf ', 'vwd w', 'GF', 'eee'),
(62, 'wdvw', 'BBB', 'BCC', '1', '1'),
(63, 'vwvw', '', 'vepvend', '', ''),
(64, 'vwvw', '', 'vepvend', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `topuphistory`
--

CREATE TABLE `topuphistory` (
  `id` int(11) NOT NULL,
  `UserID` varchar(50) NOT NULL,
  `CardID` int(20) NOT NULL,
  `BusRoute` varchar(50) NOT NULL,
  `BusID` varchar(20) NOT NULL,
  `Amount` int(20) NOT NULL,
  `Recharge_Date` varchar(20) NOT NULL,
  `week` int(50) NOT NULL,
  `Recharge_Time` varchar(20) NOT NULL,
  `CardReaderID` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `topuphistory`
--

INSERT INTO `topuphistory` (`id`, `UserID`, `CardID`, `BusRoute`, `BusID`, `Amount`, `Recharge_Date`, `week`, `Recharge_Time`, `CardReaderID`) VALUES
(1, 'cr00000001', 1235, '1A', 'S12345678', 200, '2018-01-01', 1, '9:30 AM', '5.32.45.85'),
(2, 'cr00000002', 6523, '2B', 'S98765432', 130, '2018-01-03', 1, '8:15 PM', '5.32.45.85'),
(3, 'cr00000003', 1549, '1A', 'S12345678', 120, '2018-12-01', 48, '9:17 AM', '3.65.48.52'),
(4, 'cr00000004', 2359, '2B', 'S98765432', 45, '2018-12-03', 48, '8:45 PM', '3.65.48.52');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `UserID` varchar(20) DEFAULT NULL,
  `FirstName` varchar(20) NOT NULL,
  `LastName` varchar(20) NOT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Mobile` varchar(20) DEFAULT NULL,
  `City` varchar(20) DEFAULT NULL,
  `State` varchar(20) DEFAULT NULL,
  `Pincode` int(20) DEFAULT NULL,
  `SignUp_Type` varchar(20) DEFAULT NULL,
  `isEVP` int(20) DEFAULT NULL,
  `isOTP` int(20) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Created_Date` varchar(20) DEFAULT NULL,
  `Updated_Date` varchar(20) DEFAULT NULL,
  `CardNumber` int(20) DEFAULT NULL,
  `FareType` int(50) DEFAULT NULL,
  `Age` varchar(50) DEFAULT NULL,
  `Gender` varchar(50) DEFAULT NULL,
  `MaritalStatus` varchar(20) DEFAULT NULL,
  `Profession` varchar(20) DEFAULT NULL,
  `FirstNameAC` varchar(20) DEFAULT NULL,
  `LastNameAC` varchar(50) DEFAULT NULL,
  `Account_Number` int(20) DEFAULT NULL,
  `IFSC` int(20) DEFAULT NULL,
  `AddressLine1` varchar(20) DEFAULT NULL,
  `AddressLine2` varchar(50) DEFAULT NULL,
  `AddressLine3` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `UserID`, `FirstName`, `LastName`, `Email`, `Mobile`, `City`, `State`, `Pincode`, `SignUp_Type`, `isEVP`, `isOTP`, `Password`, `Created_Date`, `Updated_Date`, `CardNumber`, `FareType`, `Age`, `Gender`, `MaritalStatus`, `Profession`, `FirstNameAC`, `LastNameAC`, `Account_Number`, `IFSC`, `AddressLine1`, `AddressLine2`, `AddressLine3`) VALUES
(1, 'cr00000001', 'Barathram', 'Kunka', 'kunka@gmail.com', '1915352641', 'singUung', 'very good', 5766967, 'app', 1236, 2654, 'asdf1234', '3-Dec-18', '18-Sep-5', 1235, 1, '22', 'M', 'Ok school', 'S98765432t', 'john', 'jil', 12, 12, 'ochob science', '', ''),
(2, 'cr00000002', 'jane', 'mol', 'jane.mol@gmail.com', '+915967845212', 'kolkata', 'west bengal', 700012, 'facebook', 1532, 3295, 'edfg1265', '18-May-18', '', 6523, 1, '32-50', 'F', 'S', 'E', 'jane', 'mol', 26856, 22145, '235 vds kol 06', '', ''),
(3, 'cr00000003', 'das', 'but', 'das.but@gmail.com', '+913956854123', 'mumbai', 'maharashtra', 400003, 'facebook', 1546, 6548, 'edfg5469', '16-Dec-18', '', 1549, 2, '32-50', 'F', 'S', 'E', 'das', 'but', 0, 0, '264 ave', '', ''),
(4, 'cr00000004', '', '', '', '', 'kolkata', 'west bengal', 0, '', 0, 0, '', '15-Jun-18', '', 2359, 3, '60-70', 'M', '', '', '', '', 0, 0, '', '', ''),
(5, NULL, 'Dx', 'ST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '17-Sep-18', NULL, 633565, 2, '18', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(6, NULL, 'Dx', 'ST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '17-Sep-18', NULL, 633567, 2, '18', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(7, NULL, 'Dx', 'ST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '17-Sep-18', NULL, 63289567, 2, '18', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(8, NULL, 'Dx', 'ST', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '17-Sep-18', NULL, 285133, 2, '18', 'M', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bus`
--
ALTER TABLE `bus`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `busroute`
--
ALTER TABLE `busroute`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `busstops`
--
ALTER TABLE `busstops`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `card`
--
ALTER TABLE `card`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `driver`
--
ALTER TABLE `driver`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dtransaction`
--
ALTER TABLE `dtransaction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reportblock`
--
ALTER TABLE `reportblock`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reportchangedevice`
--
ALTER TABLE `reportchangedevice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reportchangeroute`
--
ALTER TABLE `reportchangeroute`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reportservice`
--
ALTER TABLE `reportservice`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ridehistory`
--
ALTER TABLE `ridehistory`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `routefare`
--
ALTER TABLE `routefare`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `topuphistory`
--
ALTER TABLE `topuphistory`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bus`
--
ALTER TABLE `bus`
  MODIFY `id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `busroute`
--
ALTER TABLE `busroute`
  MODIFY `id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `busstops`
--
ALTER TABLE `busstops`
  MODIFY `id` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `card`
--
ALTER TABLE `card`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `driver`
--
ALTER TABLE `driver`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `dtransaction`
--
ALTER TABLE `dtransaction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `reportblock`
--
ALTER TABLE `reportblock`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `reportchangedevice`
--
ALTER TABLE `reportchangedevice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `reportchangeroute`
--
ALTER TABLE `reportchangeroute`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `reportservice`
--
ALTER TABLE `reportservice`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `ridehistory`
--
ALTER TABLE `ridehistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `routefare`
--
ALTER TABLE `routefare`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;

--
-- AUTO_INCREMENT for table `topuphistory`
--
ALTER TABLE `topuphistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
