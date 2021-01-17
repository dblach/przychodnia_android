-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 17 Sty 2021, 21:23
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
(3, 1, 1, 2, '09:00:00', '10:00:00', '2'),
(4, 1, 1, 2, '14:00:00', '16:00:00', '2');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `konfiguracja`
--

CREATE TABLE `konfiguracja` (
  `ustawienie` varchar(30) NOT NULL,
  `wartosc` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `konfiguracja`
--

INSERT INTO `konfiguracja` (`ustawienie`, `wartosc`) VALUES
('logo', 'icon_main.png'),
('tekst_powitalny', 'Zapraszamy!'),
('czas_na_edycje', '2');

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
(5, 'Dominik', 'Błach', '2019-01-21', 'Test123\nTest456', 'kborowiecki');

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
-- Struktura tabeli dla tabeli `wiadomosci`
--

CREATE TABLE `wiadomosci` (
  `id` int(11) NOT NULL,
  `nazwa` varchar(50) NOT NULL,
  `tresc` text NOT NULL,
  `obraz` varchar(30) NOT NULL,
  `data_dodania` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `wiadomosci`
--

INSERT INTO `wiadomosci` (`id`, `nazwa`, `tresc`, `obraz`, `data_dodania`) VALUES
(1, 'test długiej wiadomości', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam non metus a velit iaculis aliquet. Ut molestie turpis mi, sed varius nibh faucibus ac. Suspendisse nec ultrices mi, cursus cursus odio. Pellentesque id justo mi. Pellentesque pellentesque consectetur fringilla. Quisque in tincidunt arcu. Duis lacinia tincidunt lacus, nec viverra ipsum placerat at. Fusce eget mauris ut metus placerat rutrum. Etiam convallis posuere gravida. Sed mauris libero, lobortis et lacinia sed, faucibus sit amet massa. In a diam sollicitudin, egestas ante et, pretium quam. Pellentesque sit amet odio velit. Phasellus ac nibh aliquet, interdum elit ac, feugiat urna.\r\n\r\nUt in velit vitae turpis fringilla suscipit. Cras consequat maximus purus hendrerit mattis. Fusce faucibus finibus nunc nec ultricies. Vivamus ullamcorper justo nec ornare porta. Ut ut nulla finibus, viverra sem eget, elementum nulla. Donec euismod sapien et tortor volutpat cursus. Pellentesque suscipit id enim vitae fermentum. Nam a nisl vestibulum, euismod velit eu, fermentum lacus. Morbi a metus quis sapien cursus consequat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Cras id magna vitae mi pretium rutrum quis nec sem. Curabitur quis ex non sem mollis consectetur. Aliquam sed orci nec nisi dictum tempor vulputate ac sapien. Curabitur fringilla vel lorem ut viverra. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\r\n\r\nPhasellus hendrerit in velit vel porttitor. Phasellus feugiat consequat ante vel pretium. Sed vel lobortis lacus. Mauris nec tristique dui. Mauris feugiat, justo sed molestie vestibulum, augue nibh malesuada leo, vitae imperdiet ipsum tellus a nunc. Sed sed ante enim. Nam dui ex, consequat non arcu nec, finibus semper nisl. Donec mi enim, accumsan et fringilla eu, cursus vitae velit.\r\n\r\nNulla eleifend ullamcorper nibh. Phasellus molestie a augue at interdum. Nulla venenatis nec erat in euismod. In lacinia tortor sed hendrerit pretium. Integer non lorem magna. Proin ut orci id diam posuere pretium ut ac ipsum. Ut placerat ligula quis fermentum tincidunt. Morbi non nisl mauris. Donec rutrum ullamcorper sagittis. Fusce iaculis mattis dignissim.\r\n\r\nNullam condimentum lectus nec dui sodales ultrices. Suspendisse et metus ut lorem aliquet hendrerit eu a diam. Nulla scelerisque sodales justo ac tincidunt. Integer ornare gravida eros tempus sagittis. Vestibulum in auctor magna. Integer varius mauris sit amet lacus pulvinar convallis eu a quam. Donec malesuada eget ipsum sit amet malesuada.', 'lorem.jpg', '2020-10-14 14:06:20'),
(2, 'Teleporada', 'Przychodnia udziela teleporad pod nr tel. 123456789.', 'phone.jpg', '2020-11-06 17:16:04'),
(3, 'Zmiana godzin otwarcia', 'Od 9 listopada przychodnia będzie czynna w godzinach 10-16.', 'clock.jpg', '2020-11-08 17:29:34');

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
(12, 5, 1, '2020-05-21', '16:00:00', '16:30:00', '', 0),
(19, 5, 1, '2020-06-02', '09:30:00', '10:00:00', '', 0),
(20, 5, 1, '2020-06-02', '15:00:00', '15:30:00', '', 0),
(21, 5, 2, '2020-06-15', '11:00:00', '11:30:00', '', 0),
(35, 5, 1, '2020-06-16', '15:00:00', '15:30:00', '', 0),
(36, 5, 1, '2020-06-23', '14:30:00', '15:00:00', '', 0),
(38, 5, 1, '2020-06-23', '09:00:00', '09:30:00', '', 0),
(39, 5, 2, '2020-10-12', '10:30:00', '11:00:00', 'Aspiryna 2x dziennie\r\nIbuprom 1x dziennie', 0),
(40, 5, 2, '2020-10-12', '11:30:00', '12:00:00', '', 0),
(41, 5, 1, '2020-12-08', '09:00:00', '09:30:00', '', 1),
(42, 5, 2, '2020-12-07', '11:30:00', '12:00:00', '', 0),
(43, 5, 2, '2020-12-16', '13:00:00', '13:30:00', '', 1),
(44, 5, 1, '2020-12-15', '09:30:00', '10:00:00', '', 1),
(45, 5, 2, '2020-12-14', '10:30:00', '11:00:00', '', 0),
(46, 5, 1, '2021-01-19', '09:00:00', '09:30:00', '', 0),
(47, 5, 1, '2021-01-19', '14:00:00', '14:30:00', '', 0);

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
-- Indeksy dla tabeli `wiadomosci`
--
ALTER TABLE `wiadomosci`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pacjent_id` (`pacjent_id`),
  ADD KEY `lekarz_id` (`lekarz_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `godziny_przyjec`
--
ALTER TABLE `godziny_przyjec`
  MODIFY `id_przyjecia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
-- AUTO_INCREMENT dla tabeli `wiadomosci`
--
ALTER TABLE `wiadomosci`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `godziny_przyjec`
--
ALTER TABLE `godziny_przyjec`
  ADD CONSTRAINT `godziny_przyjec_ibfk_1` FOREIGN KEY (`id_lekarza`) REFERENCES `lekarze` (`id`),
  ADD CONSTRAINT `godziny_przyjec_ibfk_2` FOREIGN KEY (`specjalizacja`) REFERENCES `specjalizacje` (`id`);

--
-- Ograniczenia dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  ADD CONSTRAINT `wizyty_ibfk_1` FOREIGN KEY (`lekarz_id`) REFERENCES `lekarze` (`id`),
  ADD CONSTRAINT `wizyty_ibfk_2` FOREIGN KEY (`pacjent_id`) REFERENCES `pacjenci` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
