# Rezervari-clinica-medicala
Aplicație Java Swing pentru rezervări la clinică (login admin/pacient, CRUD doctori, programări, Apache Derby, Maven, JUnit

## Funcționalități
- Autentificare pacient / administrator
- Administrare doctori (adăugare / modificare)
- Creare programare la doctor selectat (data + ora)
- Anulare programare
- Regula: nu permite suprapuneri pentru același doctor la aceeași dată/oră
- Persistență date în Apache Derby (embedded) + inițializare automată schema
- Test JUnit pentru regula de suprapunere

## Tehnologii
Java, Swing, Maven, JDBC, Apache Derby (embedded), JUnit 5


---

## Structura proiectului

```text
clinic-reservations/
├─ pom.xml
├─ README.md
├─ src/
│  ├─ main/
│  │  └─ java/
│  │     └─ ro/
│  │        └─ clinicrezervari/
│  │           ├─ app/
│  │           │  └─ App.java
│  │           ├─ db/
│  │           │  ├─ DatabaseManager.java
│  │           │  ├─ DoctorDao.java
│  │           │  ├─ PacientDao.java
│  │           │  └─ ProgramareDao.java
│  │           ├─ model/
│  │           │  ├─ Administrator.java
│  │           │  ├─ Doctor.java
│  │           │  ├─ Pacient.java
│  │           │  └─ Programare.java
│  │           ├─ service/
│  │           │  ├─ AuthService.java
│  │           │  ├─ DoctorService.java
│  │           │  └─ ProgramareService.java
│  │           └─ ui/
│  │              ├─ MainFrame.java
│  │              ├─ LoginPanel.java
│  │              ├─ AdminPanel.java
│  │              └─ PacientPanel.java
│  └─ test/
│     └─ java/
│        └─ ro/
│           └─ clinicrezervari/
│              └─ ProgramareServiceTest.java

