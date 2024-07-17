'use server'

import { cookies } from 'next/headers';
import { NextResponse } from 'next/server';

export async function GET() {
  const res = NextResponse.json({ success: true, message: 'Logout successful' });

  // Clear the token cookie
  cookies().delete('token');

  return res;
}
