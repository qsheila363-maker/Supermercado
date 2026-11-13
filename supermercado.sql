-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 30-10-2026 a las 13:01:02
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `supermercado`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('NORMAL','VIP') NOT NULL DEFAULT 'NORMAL',
  `descuento_vip` decimal(5,2) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`dni`, `nombre`, `tipo`, `descuento_vip`) VALUES
('28222333', 'Luis Gómez', 'VIP', 15.00),
('30111222', 'Ana Pérez', 'NORMAL', NULL),
('35333444', 'Marta Díaz', 'NORMAL', NULL),
('40444555', 'Sofía Ruiz', 'VIP', 10.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compra`
--

CREATE TABLE `compra` (
  `id_compra` int(11) NOT NULL,
  `dni_cliente` varchar(20) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT current_timestamp(),
  `descuento_vip` decimal(5,2) DEFAULT NULL,
  `total` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `compra`
--

INSERT INTO `compra` (`id_compra`, `dni_cliente`, `fecha`, `descuento_vip`, `total`) VALUES
(1, '30111222', '2026-10-25 10:30:00', NULL, 4840.00),
(2, '28222333', '2026-10-26 17:15:00', 15.00, 34042.50),
(3, '35333444', '2026-10-27 12:00:00', NULL, 2100.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_compra`
--

CREATE TABLE `detalle_compra` (
  `id_detalle` int(11) NOT NULL,
  `id_compra` int(11) NOT NULL,
  `codigo_producto` varchar(20) NOT NULL,
  `cantidad` int(11) NOT NULL CHECK (`cantidad` > 0),
  `precio_unitario` decimal(10,2) NOT NULL,
  `promocion` tinyint(4) NOT NULL DEFAULT 0,
  `subtotal` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detalle_compra`
--

INSERT INTO `detalle_compra` (`id_detalle`, `id_compra`, `codigo_producto`, `cantidad`, `precio_unitario`, `promocion`, `subtotal`) VALUES
(1, 1, 'H001', 2, 1320.00, 0, 2640.00),
(2, 1, 'A002', 1, 2200.00, 0, 2200.00),
(3, 2, 'E002', 1, 36750.00, 0, 31237.50),
(4, 2, 'H001', 3, 1320.00, 2, 2805.00),
(5, 3, 'A001', 4, 1050.00, 1, 2100.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `id_empleado` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `salario_base` decimal(10,2) NOT NULL CHECK (`salario_base` >= 0),
  `tipo` enum('CAJERO','GERENTE') NOT NULL,
  `ventas_realizadas` decimal(12,2) DEFAULT NULL,
  `bono_gerencial` decimal(10,2) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`id_empleado`, `nombre`, `salario_base`, `tipo`, `ventas_realizadas`, `bono_gerencial`) VALUES
('CJ0002', 'Valeria Núñez', 52000.00, 'CAJERO', 15000.00, NULL),
('HS56789JU', 'Carlos Sánchez', 50000.00, 'CAJERO', 20000.00, NULL),
('JSUF625348FH', 'Romina Martínez', 650000.00, 'GERENTE', NULL, 20000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `precio` decimal(10,2) NOT NULL CHECK (`precio` >= 0),
  `categoria` varchar(50) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0 CHECK (`stock` >= 0),
  `tipo` enum('ALIMENTICIO','ELECTRONICO','HIGIENE') NOT NULL,
  `fecha_caducidad` date DEFAULT NULL,
  `es_perecedero` tinyint(1) DEFAULT NULL,
  `garantia` int(11) DEFAULT NULL CHECK (`garantia` >= 0),
  `tipo_uso` varchar(50) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`codigo`, `nombre`, `precio`, `categoria`, `stock`, `tipo`, `fecha_caducidad`, `es_perecedero`, `garantia`, `tipo_uso`) VALUES
('A001', 'Leche entera 1L', 1500.00, 'Alimentación', 36, 'ALIMENTICIO', '2026-11-02', 1, NULL, NULL),
('A002', 'Arroz 1kg', 2200.00, 'Alimentación', 59, 'ALIMENTICIO', '2027-10-30', 0, NULL, NULL),
('A003', 'Yogur natural', 800.00, 'Alimentación', 25, 'ALIMENTICIO', '2026-11-09', 1, NULL, NULL),
('A004', 'Pan lactal', 2100.00, 'Alimentación', 15, 'ALIMENTICIO', '2026-10-28', 1, NULL, NULL),
('E001', 'Auriculares', 18000.00, 'Electrónica', 12, 'ELECTRONICO', NULL, NULL, 1, NULL),
('E002', 'Parlante Bluetooth', 35000.00, 'Electrónica', 7, 'ELECTRONICO', NULL, NULL, 3, NULL),
('E003', 'Cargador USB-C', 9000.00, 'Electrónica', 20, 'ELECTRONICO', NULL, NULL, 2, NULL),
('E004', 'Smart TV 43\"', 450000.00, 'Electrónica', 3, 'ELECTRONICO', NULL, NULL, 3, NULL),
('H001', 'Jabón de tocador', 1200.00, 'Higiene', 45, 'HIGIENE', NULL, NULL, NULL, 'personal'),
('H002', 'Shampoo', 3500.00, 'Higiene', 30, 'HIGIENE', NULL, NULL, NULL, 'personal'),
('H003', 'Detergente', 2800.00, 'Higiene', 35, 'HIGIENE', NULL, NULL, NULL, 'doméstico'),
('H004', 'Limpiador multiuso', 1800.00, 'Higiene', 40, 'HIGIENE', NULL, NULL, NULL, 'doméstico');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `compra`
--
ALTER TABLE `compra`
  ADD PRIMARY KEY (`id_compra`),
  ADD KEY `dni_cliente` (`dni_cliente`);

--
-- Indices de la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `id_compra` (`id_compra`),
  ADD KEY `codigo_producto` (`codigo_producto`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`id_empleado`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`codigo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `compra`
--
ALTER TABLE `compra`
  MODIFY `id_compra` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  MODIFY `id_detalle` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `compra`
--
ALTER TABLE `compra`
  ADD CONSTRAINT `compra_ibfk_1` FOREIGN KEY (`dni_cliente`) REFERENCES `cliente` (`dni`);

--
-- Filtros para la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  ADD CONSTRAINT `detalle_compra_ibfk_1` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`) ON DELETE CASCADE,
  ADD CONSTRAINT `detalle_compra_ibfk_2` FOREIGN KEY (`codigo_producto`) REFERENCES `producto` (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
