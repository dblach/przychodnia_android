-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 31 Maj 2020, 00:21
-- Wersja serwera: 10.4.11-MariaDB
-- Wersja PHP: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `eskulap`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `godziny_przyjec`
--

CREATE TABLE `godziny_przyjec` (
  `id_przyjecia` int(11) NOT NULL,
  `id_lekarza` int(11) NOT NULL,
  `specjalizacja` int(11) NOT NULL,
  `dzien_tygodnia` int(11) NOT NULL,
  `godzina_rozpoczecia` time NOT NULL,
  `godzina_zakonczenia` time NOT NULL,
  `pomieszczenie` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `godziny_przyjec`
--

INSERT INTO `godziny_przyjec` (`id_przyjecia`, `id_lekarza`, `specjalizacja`, `dzien_tygodnia`, `godzina_rozpoczecia`, `godzina_zakonczenia`, `pomieszczenie`) VALUES
(1, 2, 1, 1, '10:00:00', '12:00:00', '3'),
(2, 2, 1, 3, '12:00:00', '14:00:00', '3'),
(3, 1, 1, 2, '09:00:00', '10:00:00', '2');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lekarze`
--

CREATE TABLE `lekarze` (
  `id` int(11) NOT NULL,
  `nazwisko` text COLLATE utf8_polish_ci NOT NULL,
  `imie` text COLLATE utf8_polish_ci NOT NULL,
  `specjalizacja` int(11) NOT NULL,
  `uprawnienia` int(11) NOT NULL,
  `nazwa` text COLLATE utf8_polish_ci NOT NULL,
  `zdjecie` varchar(30) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `lekarze`
--

INSERT INTO `lekarze` (`id`, `nazwisko`, `imie`, `specjalizacja`, `uprawnienia`, `nazwa`, `zdjecie`) VALUES
(1, 'Wilczur', 'Rafał', 1, 2, 'rwilczur', 'rwilczur.jpg'),
(2, 'House', 'Gregory', 1, 1, 'ghouse', 'ghouse.jpg'),
(3, 'Kotek', 'Zdzisław', 2, 1, 'zkotek', 'zkotek.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pacjenci`
--

CREATE TABLE `pacjenci` (
  `id` int(11) NOT NULL,
  `imie` text COLLATE utf8_polish_ci NOT NULL,
  `nazwisko` text COLLATE utf8_polish_ci NOT NULL,
  `data_zapisania` date NOT NULL,
  `notatki` text COLLATE utf8_polish_ci NOT NULL,
  `login` varchar(30) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `pacjenci`
--

INSERT INTO `pacjenci` (`id`, `imie`, `nazwisko`, `data_zapisania`, `notatki`, `login`) VALUES
(1, 'Jan', 'Kowalski', '2018-12-11', 'bla1\nbla2', ''),
(2, 'Adam', 'Nowak', '2019-01-01', '', ''),
(5, 'Karol', 'Borowiecki', '2019-01-21', 'Test123\nTest456', 'kborowiecki');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `specjalizacje`
--

CREATE TABLE `specjalizacje` (
  `id` int(11) NOT NULL,
  `nazwa` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `ikona` varchar(30) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `specjalizacje`
--

INSERT INTO `specjalizacje` (`id`, `nazwa`, `ikona`) VALUES
(1, 'Choroby wewnętrzne', 'internistyczna.png'),
(2, 'Laryngologia', 'laryngologiczna.png'),
(4, 'Stomatologia', 'stomatologiczna.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wizyty`
--

CREATE TABLE `wizyty` (
  `id` int(11) NOT NULL,
  `pacjent_id` int(11) NOT NULL,
  `lekarz_id` int(11) NOT NULL,
  `data` date NOT NULL,
  `czas_rozpoczecia` time NOT NULL,
  `czas_zakonczenia` time NOT NULL,
  `notatka` text COLLATE utf8_polish_ci NOT NULL,
  `odwolana` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `wizyty`
--

INSERT INTO `wizyty` (`id`, `pacjent_id`, `lekarz_id`, `data`, `czas_rozpoczecia`, `czas_zakonczenia`, `notatka`, `odwolana`) VALUES
(1, 1, 1, '2019-01-01', '12:00:00', '13:00:00', '', 0),
(2, 1, 2, '2019-01-01', '15:00:00', '16:00:00', 'test', 0),
(3, 1, 2, '2019-01-01', '16:00:00', '16:30:00', '', 0),
(9, 1, 2, '2019-01-19', '00:00:00', '01:00:00', '', 0),
(10, 1, 2, '2019-01-19', '03:00:00', '05:00:00', 'fgsefg', 0),
(11, 5, 2, '2019-01-23', '09:00:00', '10:00:00', '', 0),
(12, 5, 1, '2020-05-21', '16:00:00', '16:30:00', '', 0);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `godziny_przyjec`
--
ALTER TABLE `godziny_przyjec`
  ADD PRIMARY KEY (`id_przyjecia`),
  ADD KEY `id_lekarza` (`id_lekarza`),
  ADD KEY `specjalizacja` (`specjalizacja`);

--
-- Indeksy dla tabeli `lekarze`
--
ALTER TABLE `lekarze`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `pacjenci`
--
ALTER TABLE `pacjenci`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `specjalizacje`
--
ALTER TABLE `specjalizacje`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `godziny_przyjec`
--
ALTER TABLE `godziny_przyjec`
  MODIFY `id_przyjecia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `lekarze`
--
ALTER TABLE `lekarze`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `pacjenci`
--
ALTER TABLE `pacjenci`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT dla tabeli `specjalizacje`
--
ALTER TABLE `specjalizacje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `godziny_przyjec`
--
ALTER TABLE `godziny_przyjec`
  ADD CONSTRAINT `godziny_przyjec_ibfk_1` FOREIGN KEY (`id_lekarza`) REFERENCES `lekarze` (`id`),
  ADD CONSTRAINT `godziny_przyjec_ibfk_2` FOREIGN KEY (`specjalizacja`) REFERENCES `specjalizacje` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
