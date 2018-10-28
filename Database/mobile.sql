-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 28, 2018 at 11:10 AM
-- Server version: 10.1.23-MariaDB-9+deb9u1
-- PHP Version: 7.0.30-0+deb9u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mobile`
--

-- --------------------------------------------------------

--
-- Table structure for table `main`
--

CREATE TABLE `main` (
  `id` int(10) UNSIGNED NOT NULL,
  `appVersion` text COLLATE utf8_turkish_ci NOT NULL,
  `serviceCheck` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Dumping data for table `main`
--

INSERT INTO `main` (`id`, `appVersion`, `serviceCheck`) VALUES
(1, '1.0', 'care');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) UNSIGNED NOT NULL,
  `userName` text COLLATE utf8_turkish_ci NOT NULL,
  `userMail` text COLLATE utf8_turkish_ci NOT NULL,
  `userPassword` text COLLATE utf8_turkish_ci NOT NULL,
  `lastLogin` text COLLATE utf8_turkish_ci NOT NULL,
  `deviceToken` text COLLATE utf8_turkish_ci NOT NULL,
  `accountVerification` text COLLATE utf8_turkish_ci NOT NULL,
  `verificationCode` text COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `main`
--
ALTER TABLE `main`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `main`
--
ALTER TABLE `main`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
