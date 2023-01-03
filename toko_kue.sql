-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Jan 03, 2023 at 02:10 AM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toko_kue`
--

-- --------------------------------------------------------

--
-- Table structure for table `laporan`
--

CREATE TABLE `laporan` (
  `no_pemesanan` varchar(50) NOT NULL,
  `tgl_pemesanan` date NOT NULL,
  `nama_konsumen` varchar(50) CHARACTER SET latin1 NOT NULL,
  `alamat_konsumen` longtext NOT NULL,
  `telp_konsumen` varchar(20) NOT NULL,
  `nama_barang` varchar(50) CHARACTER SET latin1 NOT NULL,
  `tipe_barang` varchar(20) NOT NULL,
  `ukuran_barang` varchar(20) NOT NULL,
  `jumlah_barang` int(10) NOT NULL,
  `harga_barang` int(15) NOT NULL,
  `subtotal_harga` int(15) NOT NULL,
  `total_harga` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `laporan`
--

INSERT INTO `laporan` (`no_pemesanan`, `tgl_pemesanan`, `nama_konsumen`, `alamat_konsumen`, `telp_konsumen`, `nama_barang`, `tipe_barang`, `ukuran_barang`, `jumlah_barang`, `harga_barang`, `subtotal_harga`, `total_harga`) VALUES
('ORDERPJ221', '2022-07-26', 'Damara Syaidil', 'Elok', '081319916659', 'test', 'test', '2x2', 5, 5000, 25000, 65000),
('ORDERPJ221', '2022-07-26', 'Damara Syaidil', 'Elok', '081319916659', 'apa aja', 'bebas', 'bebas', 2, 20000, 40000, 65000),
('ORDERPJ223', '2022-07-26', 'rakha', 'gasgasdasdasd gasdasdasd gasdasd gasgasdw', '08129281787727', 'jkjkjkjkjk', 'kjkjkljasd', 'kljkljglkasd', 3, 3000, 9000, 11000),
('ORDERPJ223', '2022-07-26', 'rakha', 'gasgasdasdasd gasdasdasd gasdasd gasgasdw', '08129281787727', 'kljgklasd', 'kldfjg', 'dfgklj', 1, 2000, 2000, 11000),
('ORDERPJ225', '2022-07-26', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'test', 'test', 'test', 2, 20000, 40000, 40000),
('ORDERPJ226', '2022-07-28', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'tes2', 'tes2', 'test', 2, 4, 8, 112),
('ORDERPJ226', '2022-07-28', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'test2', 'test3', 'tes2', 5, 20, 100, 112),
('ORDERPJ228', '2022-07-30', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'test', 'test', 'test', 2, 2000, 4000, 214000),
('ORDERPJ228', '2022-07-30', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'damara', 'damara', 'damara', 5, 2000, 10000, 214000),
('ORDERPJ228', '2022-07-30', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'jhjlhjkdhfgjkdfhgjkdfhgjkdfhgjkdfh', 'jkhsjkdfhjskdf', 'jklshdfjkhsgj', 2, 100000, 200000, 214000),
('ORDERPJ2211', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'oajifljasidljasldjaisd', 'klajsdklajsklaf', 'klajsdklasjd', 2, 100000, 200000, 300000),
('ORDERPJ2211', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'iahsdjlhajlhfjalshdjlasd', 'jlahdjlshajld', 'ajlhsas', 5, 20000, 100000, 300000),
('ORDERPJ2213', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'oajifljasidljasldjaisd', 'klajsdklajsklaf', 'klajsdklasjd', 2, 100000, 200000, 300000),
('ORDERPJ2213', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'iahsdjlhajlhfjalshdjlasd', 'jlahdjlshajld', 'ajlhsas', 5, 20000, 100000, 300000),
('ORDERPJ2215', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'oajifljasidljasldjaisd', 'klajsdklajsklaf', 'klajsdklasjd', 2, 100000, 200000, 300000),
('ORDERPJ2215', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'iahsdjlhajlhfjalshdjlasd', 'jlahdjlshajld', 'ajlhsas', 5, 20000, 100000, 300000),
('ORDERPJ2217', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'oajifljasidljasldjaisd', 'klajsdklajsklaf', 'klajsdklasjd', 2, 100000, 200000, 300000),
('ORDERPJ2217', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'iahsdjlhajlhfjalshdjlasd', 'jlahdjlshajld', 'ajlhsas', 5, 20000, 100000, 300000),
('ORDERPJ2219', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'oajifljasidljasldjaisd', 'klajsdklajsklaf', 'klajsdklasjd', 2, 100000, 200000, 300000),
('ORDERPJ2219', '2022-07-30', 'Damara Syaidil', 'Elok', '081319916659', 'iahsdjlhajlhfjalshdjlasd', 'jlahdjlshajld', 'ajlhsas', 5, 20000, 100000, 300000);

-- --------------------------------------------------------

--
-- Table structure for table `pelanggan`
--

CREATE TABLE `pelanggan` (
  `id_konsumen` varchar(50) CHARACTER SET latin1 NOT NULL,
  `nama_konsumen` varchar(50) CHARACTER SET latin1 NOT NULL,
  `telp_konsumen` varchar(20) DEFAULT NULL,
  `alamat_konsumen` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pelanggan`
--

INSERT INTO `pelanggan` (`id_konsumen`, `nama_konsumen`, `telp_konsumen`, `alamat_konsumen`) VALUES
('PJ20221', 'Damara', '081319916659', 'Test\nTest 2\nTest 3 askjgkajskdjaksd'),
('PJ20222', 'Rakha', '08129281787727', 'gasgasdasdasd gasdasdasd gasdasd\ngasgasdw'),
('PJ20223', 'Damara Syaidil', '081319916659', 'Elok');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id` int(11) NOT NULL,
  `no_pemesanan` varchar(50) NOT NULL,
  `tgl_pemesanan` date NOT NULL,
  `nama_konsumen` varchar(50) CHARACTER SET latin1 NOT NULL,
  `alamat_konsumen` longtext NOT NULL,
  `telp_konsumen` varchar(20) NOT NULL,
  `nama_barang` varchar(50) CHARACTER SET latin1 NOT NULL,
  `tipe_barang` varchar(20) NOT NULL,
  `ukuran_barang` varchar(20) NOT NULL,
  `jumlah_barang` int(10) NOT NULL,
  `harga_barang` int(15) NOT NULL,
  `subtotal_harga` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`id`, `no_pemesanan`, `tgl_pemesanan`, `nama_konsumen`, `alamat_konsumen`, `telp_konsumen`, `nama_barang`, `tipe_barang`, `ukuran_barang`, `jumlah_barang`, `harga_barang`, `subtotal_harga`) VALUES
(1, 'ORDERPJ2221', '2022-07-30', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'testkajsd', 'kljgasdasd', 'tesakjkaf', 2, 2000, 4000),
(2, 'ORDERPJ2221', '2022-07-30', 'damara', 'Test Test 2 Test 3 askjgkajskdjaksd', '081319916659', 'gasdagasdasadfgdfdfgdf', 'dfhdfgdfgdfgdhdf', 'dfgdfhdfgdfg', 3, 100000, 300000);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`) VALUES
('admin', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pelanggan`
--
ALTER TABLE `pelanggan`
  ADD PRIMARY KEY (`id_konsumen`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
