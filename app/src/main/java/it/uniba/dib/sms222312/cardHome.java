package it.uniba.dib.sms222312;

public class cardHome {
        private int image;
        private String title;

        public cardHome(int image, String title) {
            this.image = image;
            this.title = title;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
}
