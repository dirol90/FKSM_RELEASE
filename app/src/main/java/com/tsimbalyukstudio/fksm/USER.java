package com.tsimbalyukstudio.fksm;

class USER {
    private String firstName;
    private String secondName;
    private String thirdName;
    private String pasportID;
    private String pasportNum;
    private String INN;
    private String phone;
    private String phonePoruch;
    private String ccHolder;
    private String creditCard;
    private String ccValid;
    private String CVV;
    private String birthday;
    private String characterStatus;
    private String email;
    private String password;

    USER() {
        firstName = "";
        secondName = "";
        thirdName = "";
        pasportID = "";
        pasportNum = "";
        INN = "";
        phone = "";
        phonePoruch = "";
        ccHolder = "";
        creditCard = "";
        ccValid = "";
        CVV = "";
        birthday = "";
        email = "" + UserCreatePhotos.email;
        password = "" + UserCreatePhotos.password;
        characterStatus = "3";
    }

    USER (int x){
        firstName = "" + UserCreateInfo.firstName.getText().toString();
        secondName = "" + UserCreateInfo.secondName.getText().toString();
        thirdName = "" + UserCreateInfo.thirdName.getText().toString();
        pasportID = "" + UserCreateInfo.pasportID.getText().toString();
        pasportNum = "" + UserCreateInfo.pasportNum.getText().toString();
        INN = "" + UserCreateInfo.INN.getText().toString();
        phone = "" + UserCreateInfo.phone.getText().toString();
        phonePoruch = "" + UserCreateInfo.phonePoruch.getText().toString();
        ccHolder = "" + UserCreateInfo.ccHolder.getText().toString();
        creditCard = UserCreateInfo.ccFirst.getText().toString()+"-"+UserCreateInfo.ccSecond.getText().toString()+"-"+UserCreateInfo.ccThird.getText().toString()+"-"+UserCreateInfo.ccForth.getText().toString();
        ccValid = "" + UserCreateInfo.ccMonth.getText().toString()+"/"+UserCreateInfo.ccYear.getText().toString();
        CVV = "" + UserCreateInfo.CVV.getText().toString();
        birthday = UserCreateInfo.spin_day.getSelectedItem().toString()+"/"+UserCreateInfo.spin_month.getSelectedItem().toString()+"/"+UserCreateInfo.spin_year.getSelectedItem().toString();
        email = "" + UserCreateInfo.email;
        password = "" + UserCreateInfo.password;
        characterStatus = "3";
        /**
         * character status 3 == new
         * character status 2 == refused
         * character status 1 == confirmed
         */
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getPasportID() {
        return pasportID;
    }

    public void setPasportID(String pasportID) {
        this.pasportID = pasportID;
    }

    public String getPasportNum() {
        return pasportNum;
    }

    public void setPasportNum(String pasportNum) {
        this.pasportNum = pasportNum;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonePoruch() {
        return phonePoruch;
    }

    public void setPhonePoruch(String phonePoruch) {
        this.phonePoruch = phonePoruch;
    }

    public String getCcHolder() {
        return ccHolder;
    }

    public void setCcHolder(String ccHolder) {
        this.ccHolder = ccHolder;
    }

    public String getCc() {
        return creditCard;
    }

    public void setCc(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getCcValid() {
        return ccValid;
    }

    public void setCcValid(String ccValid) {
        this.ccValid = ccValid;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCharacterStatus() {
        return characterStatus;
    }

    public void setCharacterStatus(String characterStatus) {
        this.characterStatus = characterStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


