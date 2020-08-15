<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class pesananDetail extends Model
{
    public function master(){
        return $this->belongsTo('App\pesananMaster','bahan_pokok_id','bahan_pokok_id');
    }

    public function order(){
        return $this->hasOne('App\makanan','makanan_id','makanan_id');
    }
}
