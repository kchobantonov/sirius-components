/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/
import React from 'react';
import PropTypes from 'prop-types';

import { classNames } from '../../common/classnames';

import { Card, Body } from '../cards/Card';

import './InfoCard.css';

const propTypes = {
  title: PropTypes.string.isRequired,
  message: PropTypes.string
};

const INFOCARD__CLASS_NAMES = 'infocard';
const INFOCARD_TITLE__CLASS_NAMES = 'infocard-title';
const INFOCARD_MESSAGE__CLASS_NAMES = 'infocard-message';

/**
 * The InfoCard component is ued to display some information with a catchy card.
 */
export const InfoCard = ({ className, title, message, ...props }) => {
  const infoCardClassNames = classNames(INFOCARD__CLASS_NAMES, className);
  return (
    <Card {...props} className={infoCardClassNames}>
      <Body>
        <h1 className={INFOCARD_TITLE__CLASS_NAMES}>{title}</h1>
        <p className={INFOCARD_MESSAGE__CLASS_NAMES}>{message}</p>
      </Body>
    </Card>
  );
};
InfoCard.propTypes = propTypes;
