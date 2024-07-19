"use client";

import React, { useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import styles from "./Carousel.module.css";

type CarouselProps = {
  images: string[];
  title: string;
};

const Carousel: React.FC<CarouselProps> = ({ images, title }) => {
  const [nav1, setNav1] = useState<Slider | null>(null);
  const [nav2, setNav2] = useState<Slider | null>(null);

  const settingsMain = {
    dots: false,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    asNavFor: nav2 as Slider,
    ref: (slider1: Slider) => setNav1(slider1),
  };

  const settingsThumbs = {
    slidesToShow: 4,
    slidesToScroll: 1,
    arrows: false,
    dots: true,
    focusOnSelect: true,
    asNavFor: nav1 as Slider,
    ref: (slider2: Slider) => setNav2(slider2),
    centerMode: false,
    infinite: false, // Prevent infinite scrolling
  };

  return (
    <div className={styles.carouselContainer}>
      <Slider {...settingsMain} className={styles.carousel}>
        {images.map((image, index) => (
          <div key={index} className={styles.slide}>
            <img
              src={image}
              alt={`${title} Image ${index + 1}`}
              className={styles.image}
            />
          </div>
        ))}
      </Slider>
      <Slider {...settingsThumbs} className={styles.thumbs}>
        {images.map((image, index) => (
          <div key={index} className={styles.thumb}>
            <img
              src={image}
              alt={`${title} Thumbnail ${index + 1}`}
              className={styles.thumbImage}
            />
          </div>
        ))}
      </Slider>
    </div>
  );
};

export default Carousel;
