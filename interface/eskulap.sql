-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 08 Cze 2021, 19:58
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
(32, 18, 2, 1, '09:00:00', '11:00:00', '12'),
(33, 18, 1, 4, '09:00:00', '10:00:00', ''),
(34, 18, 2, 3, '10:00:00', '10:30:00', ''),
(35, 18, 1, 2, '09:30:00', '10:00:00', ''),
(36, 18, 1, 2, '11:00:00', '12:00:00', ''),
(37, 19, 1, 4, '12:00:00', '14:00:00', '12'),
(38, 19, 1, 5, '10:00:00', '12:00:00', '12');

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
('logo', '5beee9d82a83652050873b3ba4938794.jpg'),
('tekst_powitalny', 'Zapraszamy!'),
('czas_na_edycje', '3');

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
  `nazwa` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `zdjecie` varchar(36) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `lekarze`
--

INSERT INTO `lekarze` (`id`, `nazwisko`, `imie`, `specjalizacja`, `uprawnienia`, `nazwa`, `zdjecie`) VALUES
(18, 'Wilczur', 'Rafał', 2, 0, 'rwilczur1', '6f102117d65723478478dfe433982987.jpg'),
(19, 'Pawlicki', 'Doktor', 1, 0, 'dpawlicki', '673babd4bec6e4ff898c3f1cc76f168c.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pacjenci`
--

CREATE TABLE `pacjenci` (
  `id` int(11) NOT NULL,
  `imie` text COLLATE utf8_polish_ci NOT NULL,
  `nazwisko` text COLLATE utf8_polish_ci NOT NULL,
  `adres_ulica` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `adres_kod` int(11) NOT NULL,
  `adres_miasto` varchar(50) COLLATE utf8_polish_ci NOT NULL,
  `telefon` varchar(12) COLLATE utf8_polish_ci NOT NULL,
  `data_zapisania` date NOT NULL,
  `notatki` varchar(999) COLLATE utf8_polish_ci NOT NULL,
  `login` varchar(30) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `pacjenci`
--

INSERT INTO `pacjenci` (`id`, `imie`, `nazwisko`, `adres_ulica`, `adres_kod`, `adres_miasto`, `telefon`, `data_zapisania`, `notatki`, `login`) VALUES
(1, 'Jan', 'Kowalski', 'Fiołkowa 5', 12345, 'Warszawa', '', '2018-12-11', 'bla1\nbla2', 'jkowalski'),
(2, 'Adam', 'Nowak', 'Widok 4', 45678, 'Wrocław', '', '2019-01-01', '', 'anowak'),
(5, 'Dominik', 'Błach', 'Azaliowa 6', 11111, 'Szczecin', '', '2019-01-21', 'Test123\nTest456', 'kborowiecki'),
(10, 'Janusz', 'Kowalski', 'Prosta 1', 98765, 'Kraków', '', '0000-00-00', '', 'jkowal123');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `specjalizacje`
--

CREATE TABLE `specjalizacje` (
  `id` int(11) NOT NULL,
  `nazwa` varchar(30) COLLATE utf8_polish_ci NOT NULL,
  `ikona` varchar(36) COLLATE utf8_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `specjalizacje`
--

INSERT INTO `specjalizacje` (`id`, `nazwa`, `ikona`) VALUES
(1, 'Choroby wewnętrzne', '14cae9dc39d5c98923b278e899bba032.jpg'),
(2, 'Laryngologia', '83539628f756c0e605e19e476bcd7e20.jpg'),
(4, 'Stomatologia', '050baea32f262fb84624a643cb102ad1.jpg');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownicy`
--

CREATE TABLE `uzytkownicy` (
  `id` int(11) NOT NULL,
  `login` varchar(30) NOT NULL,
  `rola` enum('pacjent','lekarz','administrator','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `uzytkownicy`
--

INSERT INTO `uzytkownicy` (`id`, `login`, `rola`) VALUES
(1, 'dblach', 'administrator'),
(2, 'kborowiecki', 'pacjent'),
(6, 'testadm', 'administrator'),
(12, 'jkowal123', 'pacjent'),
(16, 'rwilczur1', 'lekarz'),
(17, 'dpawlicki', 'lekarz');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `wiadomosci`
--

CREATE TABLE `wiadomosci` (
  `id` int(11) NOT NULL,
  `nazwa` varchar(50) NOT NULL,
  `tresc` text NOT NULL,
  `obraz` varchar(36) NOT NULL,
  `data_dodania` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `wiadomosci`
--

INSERT INTO `wiadomosci` (`id`, `nazwa`, `tresc`, `obraz`, `data_dodania`) VALUES
(1, 'test długiej wiadomości', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam non metus a velit iaculis aliquet. Ut molestie turpis mi, sed varius nibh faucibus ac. Suspendisse nec ultrices mi, cursus cursus odio. Pellentesque id justo mi. Pellentesque pellentesque consectetur fringilla. Quisque in tincidunt arcu. Duis lacinia tincidunt lacus, nec viverra ipsum placerat at. Fusce eget mauris ut metus placerat rutrum. Etiam convallis posuere gravida. Sed mauris libero, lobortis et lacinia sed, faucibus sit amet massa. In a diam sollicitudin, egestas ante et, pretium quam. Pellentesque sit amet odio velit. Phasellus ac nibh aliquet, interdum elit ac, feugiat urna.\r\n\r\nUt in velit vitae turpis fringilla suscipit. Cras consequat maximus purus hendrerit mattis. Fusce faucibus finibus nunc nec ultricies. Vivamus ullamcorper justo nec ornare porta. Ut ut nulla finibus, viverra sem eget, elementum nulla. Donec euismod sapien et tortor volutpat cursus. Pellentesque suscipit id enim vitae fermentum. Nam a nisl vestibulum, euismod velit eu, fermentum lacus. Morbi a metus quis sapien cursus consequat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Cras id magna vitae mi pretium rutrum quis nec sem. Curabitur quis ex non sem mollis consectetur. Aliquam sed orci nec nisi dictum tempor vulputate ac sapien. Curabitur fringilla vel lorem ut viverra. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.\r\n\r\nPhasellus hendrerit in velit vel porttitor. Phasellus feugiat consequat ante vel pretium. Sed vel lobortis lacus. Mauris nec tristique dui. Mauris feugiat, justo sed molestie vestibulum, augue nibh malesuada leo, vitae imperdiet ipsum tellus a nunc. Sed sed ante enim. Nam dui ex, consequat non arcu nec, finibus semper nisl. Donec mi enim, accumsan et fringilla eu, cursus vitae velit.\r\n\r\nNulla eleifend ullamcorper nibh. Phasellus molestie a augue at interdum. Nulla venenatis nec erat in euismod. In lacinia tortor sed hendrerit pretium. Integer non lorem magna. Proin ut orci id diam posuere pretium ut ac ipsum. Ut placerat ligula quis fermentum tincidunt. Morbi non nisl mauris. Donec rutrum ullamcorper sagittis. Fusce iaculis mattis dignissim.\r\n\r\nNullam condimentum lectus nec dui sodales ultrices. Suspendisse et metus ut lorem aliquet hendrerit eu a diam. Nulla scelerisque sodales justo ac tincidunt. Integer ornare gravida eros tempus sagittis. Vestibulum in auctor magna. Integer varius mauris sit amet lacus pulvinar convallis eu a quam. Donec malesuada eget ipsum sit amet malesuada.', 'lorem1.jpg', '2021-06-02 21:39:34'),
(2, 'Teleporada', 'Przychodnia udziela teleporad pod nr tel. 123456789.', '04ebbc16f261e64c7152d3ddb96c9c13.jpg', '2021-06-02 21:40:34'),
(3, 'Zmiana godzin otwarcia', 'Od 9 listopada przychodnia będzie czynna w godzinach 10-16.', '77e189760987180b34dd7bc253c1d5f5.jpg', '2021-06-02 21:40:22');

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
  `notatka` varchar(1000) COLLATE utf8_polish_ci NOT NULL,
  `odwolana` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `wizyty`
--

INSERT INTO `wizyty` (`id`, `pacjent_id`, `lekarz_id`, `data`, `czas_rozpoczecia`, `czas_zakonczenia`, `notatka`, `odwolana`) VALUES
(60, 5, 18, '2021-06-01', '09:30:00', '10:00:00', '', 1),
(61, 5, 18, '2021-06-03', '09:10:00', '09:40:00', '', 0),
(62, 10, 18, '2021-06-01', '11:00:00', '11:30:00', '', 0),
(63, 1, 18, '2021-06-01', '09:30:00', '09:40:00', '', 0),
(64, 10, 19, '2021-06-10', '13:00:00', '13:30:00', '', 0);

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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nazwa` (`nazwa`);

--
-- Indeksy dla tabeli `pacjenci`
--
ALTER TABLE `pacjenci`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- Indeksy dla tabeli `specjalizacje`
--
ALTER TABLE `specjalizacje`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

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
  MODIFY `id_przyjecia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT dla tabeli `lekarze`
--
ALTER TABLE `lekarze`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT dla tabeli `pacjenci`
--
ALTER TABLE `pacjenci`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT dla tabeli `specjalizacje`
--
ALTER TABLE `specjalizacje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT dla tabeli `wiadomosci`
--
ALTER TABLE `wiadomosci`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `wizyty`
--
ALTER TABLE `wizyty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;

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
